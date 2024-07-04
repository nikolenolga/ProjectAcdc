package com.javarush.nikolenko.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hibernate.cfg.JdbcSettings.*;

public class Cnn {

    final static ApplicationProperties applicationProperties = NanoSpring.find(ApplicationProperties.class);

    static {
        try {
            Class.forName(applicationProperties.getProperty(JAKARTA_JDBC_DRIVER));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection get() {
        try {
            return DriverManager.getConnection(
                    applicationProperties.getProperty(JAKARTA_JDBC_URL),
                    applicationProperties.getProperty(JAKARTA_JDBC_USER),
                    applicationProperties.getProperty(JAKARTA_JDBC_PASSWORD)
            );
        } catch (SQLException e) {
            throw new RuntimeException("failed Connection", e);
        }
    }
}