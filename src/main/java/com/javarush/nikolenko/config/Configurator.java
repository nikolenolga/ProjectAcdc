package com.javarush.nikolenko.config;

import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestEditService;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.LoggerConstants;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
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
        log.info(LoggerConstants.START_CONFIGURATOR);
        this.userService = userService;
        this.questService = questService;
        this.questEditService = questEditService;
        this.validatorDataBase = validatorDataBase;
        fillStartData();
        log.info(LoggerConstants.FINISH_CONFIGURATOR);
    }

    @SneakyThrows
    public void fillStartData() {
        validatorDataBase.start();
        UserTo admin = getOrCreateAdminUser();
        configurateDefaultQuestsIfNeeded(admin.getId());
    }

    private UserTo getOrCreateAdminUser() {
        UserTo admin = UserTo.builder()
                .login("admin")
                .name("Admin")
                .password("admin")
                .role(Role.ADMIN)
                .build();
        admin = userService.findOrCreateUser(admin).orElseThrow();

        log.debug(LoggerConstants.USER_PICKED_UP, admin);
        return admin;
    }

    private void configurateDefaultQuestsIfNeeded(long authorId) {
        log.info(LoggerConstants.START_CONFIGURATE_DEFAULT_QUESTS);

        if (questService.countAllQuests() != 0) {
            log.info(LoggerConstants.DATA_BASE_ALLREADY_CONTAINS_QUESTS_CONFIGURATOR_SKIPED_QUESTS_PARSING);
            return;
        }
        try (Stream<Path> paths = Files.walk(questsFolder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> questEditService.loadQuest(authorId, String.valueOf(path)));
        } catch (IOException e) {
            log.error(LoggerConstants.CONFIG_DEFAULT_QUESTS_FROM_DIRECTORY_CAUSED_EXCEPTION, questsFolder, e.getMessage());
            throw new QuestException(e);
        }

        log.info(LoggerConstants.FINISH_CONFIG_DEFAULT_QUESTS);
    }

}