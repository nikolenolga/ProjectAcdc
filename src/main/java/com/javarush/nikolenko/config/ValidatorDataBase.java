package com.javarush.nikolenko.config;
import com.javarush.nikolenko.exception.QuestException;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.hibernate.cfg.JdbcSettings.*;

@Slf4j
@AllArgsConstructor
public class ValidatorDataBase {
    private final ApplicationProperties properties;
    public static final String CLASSPATH_DB_CHANGELOG_XML = "db/changelog.xml";

    public void start() {
        log.info("Running Liquibase...");

        try {
            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
                CommandScope update = new CommandScope("update");

                update.addArgumentValue("changelogFile", CLASSPATH_DB_CHANGELOG_XML);
                update.addArgumentValue("url", getProperty(JAKARTA_JDBC_URL));
                update.addArgumentValue("username", getProperty(JAKARTA_JDBC_USER));
                update.addArgumentValue("password", getProperty(JAKARTA_JDBC_PASSWORD));

                update.execute();
            });
        } catch (Exception e) {
            throw new QuestException(e);
        }

        log.info("Running Liquibase...DONE");
    }

    private String getProperty(String value) {
        return properties.getProperty(value);
    }

}
