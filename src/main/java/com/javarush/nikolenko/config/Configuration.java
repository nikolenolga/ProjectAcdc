package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.service.*;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class Configuration {
    private final QuestModifyService questModifyService;
    private final UserService userService;
    private final User admin;
    private final Path questsFolder = RequestHelper.WEB_INF.resolve(UrlHelper.QUEST_DIRECTORY);


    @SneakyThrows
    public Configuration() {
        questModifyService = ServiceLocator.getService(QuestModifyService.class);
        userService = ServiceLocator.getService(UserService.class);
        admin = new User("Admin", "admin", "admin-admin");
        userService.create(admin);
        configDefaultQuests();
    }

    private void configDefaultQuests() {
        try (Stream<Path> paths = Files.walk(questsFolder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> questModifyService.loadQuest(admin.getId(), String.valueOf(path)));
        } catch (IOException e) {

        }
    }

}