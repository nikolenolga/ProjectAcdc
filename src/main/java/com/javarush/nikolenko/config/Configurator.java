package com.javarush.nikolenko.config;

import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestEditService;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class Configurator {
    private final UserService userService;
    private final QuestService questService;
    private final QuestEditService questEditService;
    private final ValidatorDataBase validatorDataBase;
    private static final Path questsFolder = RequestHelper.WEB_INF.resolve(UrlHelper.QUEST_DIRECTORY);

    public Configurator(UserService userService, QuestService questService, QuestEditService questEditService, ValidatorDataBase validatorDataBase) {
        log.info("Start Configurator");
        this.userService = userService;
        this.questService = questService;
        this.questEditService = questEditService;
        this.validatorDataBase = validatorDataBase;
        fillStartData();
        log.info("Finish Configurator");
    }

    @SneakyThrows
    public void fillStartData() {
        validatorDataBase.start();

        int countQuests = questService.countAllQuests();
        if (countQuests == 0) {
            UserTo admin = UserTo.builder()
                    .login("admin")
                    .name("Admin")
                    .password("admin")
                    .role(Role.ADMIN)
                    .build();
            admin = userService.findOrCreateUser(admin).orElseThrow();
            log.debug("User picked up: {}", admin);

            configDefaultQuests(admin.getId());
            if((countQuests = questService.countAllQuests()) == 0) {
                log.error("Cant load quests");
                throw new QuestException("Cant load quests");
            }
            log.info("Loaded {} quests", countQuests);
        }
    }

    private void configDefaultQuests(long authorId) {
        log.info("Start config default quests");
        try (Stream<Path> paths = Files.walk(questsFolder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> questEditService.loadQuest(authorId, String.valueOf(path)));
        } catch (IOException e) {
            log.error("Config Default Quests from directory {}, caused exception - {}", questsFolder, e.getMessage());
            throw new QuestException("Can't config Default Quests");
        }
        log.info("Finish config default quests");
    }

}