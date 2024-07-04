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
import java.util.stream.Stream;

@Slf4j
public class Configurator {
    private final UserService userService;
    private final QuestService questService;
    private final QuestEditService questEditService;
    private final ValidatorDataBase validatorDataBase;
    private static final Path questsFolder = RequestHelper.WEB_INF.resolve(UrlHelper.QUEST_DIRECTORY);

    public Configurator(UserService userService, QuestService questService, QuestEditService questEditService, ValidatorDataBase validatorDataBase) {
        log.info("Configurator started");
        this.userService = userService;
        this.questService = questService;
        this.questEditService = questEditService;
        this.validatorDataBase = validatorDataBase;
        fillStartData();
        log.info("Configurator finished");
    }

    @SneakyThrows
    public void fillStartData() {
        validatorDataBase.start();

        UserTo admin = UserTo.builder()
                .login("admin")
                .name("Admin")
                .password("admin")
                .role(Role.ADMIN)
                .build();
        admin = userService.findOrCreateUser(admin).orElseThrow();
        configDefaultQuests(admin);

        if(questService.getAll().isEmpty()) {
            log.error("Cant load quests");
            throw new QuestException("Cant load quests");
        }
        log.info("Loaded {} quests", questService.getAll().size());
    }

    private void configDefaultQuests(UserTo author) {
        try (Stream<Path> paths = Files.walk(questsFolder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> questEditService.loadQuest(author, String.valueOf(path)));
        } catch (IOException e) {
            log.error("Loading quest file from directory {}, caused exception - {}", questsFolder, e.getMessage());
        }
    }

}