package com.javarush.nikolenko;

import com.javarush.nikolenko.config.ApplicationProperties;
import com.javarush.nikolenko.config.Configurator;
import com.javarush.nikolenko.config.NanoSpring;

import com.javarush.nikolenko.config.SessionCreater;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.*;

import static org.hibernate.cfg.JdbcSettings.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ContainerIT {
    private static final JdbcDatabaseContainer<?> CONTAINER;
    public static final String DOCKER_IMAGE_NAME = "postgres:16.3";

    static {
        //create
        CONTAINER = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME); //docker image loaded
        CONTAINER.start(); //docker image started
        //set new properties from TestContainers
        ApplicationProperties properties = NanoSpring.find(ApplicationProperties.class);
        properties.setProperty(JAKARTA_JDBC_URL, CONTAINER.getJdbcUrl());
        properties.setProperty(JAKARTA_JDBC_USER, CONTAINER.getUsername());
        properties.setProperty(JAKARTA_JDBC_PASSWORD, CONTAINER.getPassword());
        //fill db
        Configurator configurator = NanoSpring.find(Configurator.class);
    }

    public ContainerIT() {
        init();
    }

    public static void init() {
        log.info("test container ContainerIt initialized");
    }

    @Test
    void testSessionCreater() {
        SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
        assertNotNull(sessionCreater);
    }
}
