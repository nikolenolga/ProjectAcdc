package com.javarush.nikolenko.config;

import com.javarush.nikolenko.exception.DaoException;
import com.javarush.nikolenko.service.ImageService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;


public class ApplicationProperties extends Properties {
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
    private final File resourcesDirectory = new File("src/main/resources");
    private final String absolutePath = resourcesDirectory.getAbsolutePath();

    public final static Path CLASSES_ROOT = Paths.get(URI.create(
                    Objects.requireNonNull(
                            ApplicationProperties.class.getResource(File.separator)
                    ).toString()));

    public final static Path CLASSES_ROOT_2 = Paths.get(URI.create(
                    Objects.requireNonNull(
                            ImageService.class.getResource("/")
                    ).toString()))
            .getParent();

    public final static Path APP_PROP = CLASSES_ROOT.getParent();

    public ApplicationProperties() {
        try {
            String test_path = CLASSES_ROOT_2 + File.separator + "classes" + File.separator +"application.properties";
            System.out.println(test_path);
            String app_prop_path = CLASSES_ROOT.toString().substring(0, CLASSES_ROOT.toString().lastIndexOf("com")) + "application.properties";
            System.out.println(app_prop_path);
            this.load(new FileReader(test_path));
            String driver = this.getProperty(HIBERNATE_CONNECTION_DRIVER_CLASS);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DaoException(HIBERNATE_CONNECTION_DRIVER_CLASS + " not found");
        } catch (FileNotFoundException e) {
            throw new DaoException("Cant't find application.properties file at " + CLASSES_ROOT);
        } catch (IOException e) {
            throw new DaoException("Cant't read application.properties file at " + CLASSES_ROOT);
        }
    }
}
