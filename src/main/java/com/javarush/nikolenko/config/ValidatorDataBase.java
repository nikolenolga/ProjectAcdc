package com.javarush.nikolenko.config;
import com.javarush.nikolenko.exception.DaoException;
import com.javarush.nikolenko.exception.QuestException;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@AllArgsConstructor
public class ValidatorDataBase {
    private final ApplicationProperties properties;
    public static final String CLASSPATH_DB_CHANGELOG_XML = "db/changelog.xml";

    public void start() {
        log.debug("Running Liquibase...");

        try {
            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
                CommandScope update = new CommandScope("update");

                update.addArgumentValue("changelogFile", CLASSPATH_DB_CHANGELOG_XML);
                update.addArgumentValue("url", getProperty(ApplicationProperties.HIBERNATE_CONNECTION_URL));
                update.addArgumentValue("username", getProperty(ApplicationProperties.HIBERNATE_CONNECTION_USERNAME));
                update.addArgumentValue("password", getProperty(ApplicationProperties.HIBERNATE_CONNECTION_PASSWORD));

                update.execute();
            });
        } catch (Exception e) {
            throw new QuestException(e);
        }

        log.debug("Running Liquibase...DONE");
    }

    private String getProperty(String value) {
        return properties.getProperty(value);
    }

}
