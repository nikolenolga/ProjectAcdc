package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestModifyService;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class Configurator {
    private final UserService userService;
    private final QuestService questService;
    private final QuestModifyService questModifyService;
    private final ValidatorDataBase validatorDataBase;
    private final Path questsFolder = RequestHelper.WEB_INF.resolve(UrlHelper.QUEST_DIRECTORY);

    public Configurator(UserService userService, QuestService questService, QuestModifyService questModifyService, ValidatorDataBase validatorDataBase) {
        this.userService = userService;
        this.questService = questService;
        this.questModifyService = questModifyService;
        this.validatorDataBase = validatorDataBase;
        //fillStartData();
    }

    @SneakyThrows
    public void fillStartData() {
        validatorDataBase.start();
        User admin = null;
        Optional<User> optionalUser = userService.get(1L);
        if (optionalUser.isPresent()) {
            admin = optionalUser.get();
        } else {
            User user = User.builder()
                    .login("admin")
                    .name("Admin")
                    .password("admin")
                    .role(Role.ADMIN)
                    .build();
            admin = userService.create(user).get();
        }

        configDefaultQuests(admin);
        if(questService.getAll().isEmpty()) {
            log.error("Cant load quests");
            throw new QuestException("Cant load quests");
        }
        log.debug("Application configurated");
    }

    private void configDefaultQuests(User author) {
        try (Stream<Path> paths = Files.walk(questsFolder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> questModifyService.loadQuest(author, String.valueOf(path)));
        } catch (IOException e) {
            log.error("Loading quest file from directory {}, caused exception - {}", questsFolder, e.getMessage());
        }
    }

}