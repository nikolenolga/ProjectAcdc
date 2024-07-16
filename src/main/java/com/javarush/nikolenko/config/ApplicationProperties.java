package com.javarush.nikolenko.config;

import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.utils.LoggerConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ApplicationProperties extends Properties {
    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    @SneakyThrows
    public ApplicationProperties() {
        try {
            this.load(new FileReader(CLASSES_ROOT + File.separator + APPLICATION_PROPERTIES_FILE));
        } catch (IOException e) {
            log.error(LoggerConstants.CANT_T_FIND_OR_READ_FILE_AT, APPLICATION_PROPERTIES_FILE, CLASSES_ROOT);
            throw new QuestException(e);
        }
    }

    public final static Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    ApplicationProperties.class.getResource("/")
            ).toString()));
}