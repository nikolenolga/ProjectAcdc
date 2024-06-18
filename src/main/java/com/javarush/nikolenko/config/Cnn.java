package com.javarush.nikolenko.config;

import com.javarush.nikolenko.config.forDelete.ConfigUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Cnn implements CnnConnector {
    private static final ApplicationProperties applicationProperties = NanoSpring.find(ApplicationProperties.class);


    static {
        try {
            Class.forName(applicationProperties.getProperty(ApplicationProperties.HIBERNATE_CONNECTION_DRIVER_CLASS));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection get() {
        try {
            return DriverManager.getConnection(
                    applicationProperties.getProperty(ApplicationProperties.HIBERNATE_CONNECTION_URL),
                    applicationProperties.getProperty(ApplicationProperties.HIBERNATE_CONNECTION_USERNAME),
                    applicationProperties.getProperty(ApplicationProperties.HIBERNATE_CONNECTION_PASSWORD)
                    );
        } catch (SQLException e) {
            throw new RuntimeException("Failed connection", e);
        }
    }
}
