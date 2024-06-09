package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.service.QuestModifyService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class Configuration {
    private final QuestModifyService questModifyService;
    private final UserService userService;
    private User admin;
    private final Path questsFolder = RequestHelper.WEB_INF.resolve(UrlHelper.QUEST_DIRECTORY);


    @SneakyThrows
    public Configuration() {
        questModifyService = NanoSpring.find(QuestModifyService.class);
        userService = NanoSpring.find(UserService.class);
        configDefaultQuests();
        log.debug("Application configuration loaded");
    }

    private void configDefaultQuests() {
        LiquibaseInit.create();
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
        try (Stream<Path> paths = Files.walk(questsFolder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> questModifyService.loadQuest(admin.getId(), String.valueOf(path)));
        } catch (IOException e) {
            log.error("Loading quest file from directory {}, caused exception - {}", questsFolder, e.getMessage());
        }
    }

}