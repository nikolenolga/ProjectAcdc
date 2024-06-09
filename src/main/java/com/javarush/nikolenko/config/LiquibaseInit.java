package com.javarush.nikolenko.config;

import com.javarush.nikolenko.exception.DaoException;
import com.javarush.nikolenko.exception.QuestException;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiquibaseInit {


    public static void create() {
        log.debug("Running Liquibase...");

        try {
            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
                CommandScope update = new CommandScope("update");

                update.addArgumentValue("changelogFile", "db/changelog.xml");
                update.addArgumentValue("url", "jdbc:postgresql://localhost:5432/game");
                update.addArgumentValue("username", "postgres");
                update.addArgumentValue("password", "postgres");

                update.execute();
            });
        } catch (Exception e) {
            throw new QuestException(e);
        }

        log.debug("Running Liquibase...DONE");
    }


//    public static void main(String[] args) {
//        log.debug("Running Liquibase...");
//
//        try {
//            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
//                CommandScope update = new CommandScope("update");
//
//                update.addArgumentValue("changelogFile", "db/changelog.xml");
//                update.addArgumentValue("url", "jdbc:postgresql://localhost:5432/game");
//                update.addArgumentValue("username", "postgres");
//                update.addArgumentValue("password", "postgres");
//
//                update.execute();
//            });
//        } catch (Exception e) {
//            throw new QuestException(e);
//        }
//
//        log.debug("Running Liquibase...DONE");
//    }

}