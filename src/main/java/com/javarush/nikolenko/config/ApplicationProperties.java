package com.javarush.nikolenko.config;

import com.javarush.nikolenko.exception.DaoException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_DRIVER;

@Slf4j
public class ApplicationProperties extends Properties {
    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    @SneakyThrows
    public ApplicationProperties() {
        String pathFor = CLASSES_ROOT + File.separator + APPLICATION_PROPERTIES_FILE;
        log.info("search {} file at {} path", APPLICATION_PROPERTIES_FILE, pathFor);
        String driver = "NOT SETTED";

        try {
            this.load(new FileReader(pathFor));
            driver = this.getProperty(JAKARTA_JDBC_DRIVER);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error("{} not found, {}", driver, e);
            throw new DaoException("%s not found, %s".formatted(driver, e));
        } catch (FileNotFoundException e) {
            log.error("Cant't find {} file at {}", APPLICATION_PROPERTIES_FILE, CLASSES_ROOT);
            throw new DaoException("Cant't find %s file at %s".formatted(APPLICATION_PROPERTIES_FILE, CLASSES_ROOT));
        } catch (IOException e) {
            log.error("Cant't read {} file at {}", APPLICATION_PROPERTIES_FILE, CLASSES_ROOT);
            throw new DaoException("Cant't read %s file at %s".formatted(APPLICATION_PROPERTIES_FILE, CLASSES_ROOT));
        }
    }

    //any runtime
    public final static Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    ApplicationProperties.class.getResource("/")
            ).toString()));

    //only in Tomcat (not use in tests)
    public final static Path WEB_INF = CLASSES_ROOT.getParent();
}




//@Slf4j
//public class ApplicationProperties extends Properties {
//    private static final String CLASSES = File.separator + "classes" + File.separator;
//    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";
//
//
//    public final static Path CLASSES_ROOT = Paths.get(URI.create(
//                    Objects.requireNonNull(
//                            ApplicationProperties.class.getResource("/")
//                    ).toString()));
//
//    public final static Path APP_PROP = CLASSES_ROOT.getParent();
//
//    @SneakyThrows
//    public ApplicationProperties() {
//       String applicationPropertiesPath = CLASSES_ROOT + File.separator + APPLICATION_PROPERTIES_FILE;
//       System.out.println(applicationPropertiesPath);
//
//        String driver = null;
//        try {
//            this.load(new FileReader(applicationPropertiesPath));
////            String test_path = CLASSES_ROOT + CLASSES + APPLICATION_PROPERTIES_FILE;
////            System.out.println(test_path);
//            driver = this.getProperty(JAKARTA_JDBC_DRIVER);
//            Class.forName(driver);
//        } catch (ClassNotFoundException e) {
//            log.error("{} not found, {}", driver, e);
//            throw new DaoException("%s not found, %s".formatted(driver, e));
//        } catch (FileNotFoundException e) {
//            log.error("Cant't find {} file at {}", APPLICATION_PROPERTIES_FILE, CLASSES_ROOT);
//            throw new DaoException("Cant't find %s file at %s".formatted(APPLICATION_PROPERTIES_FILE, CLASSES_ROOT));
//        } catch (IOException e) {
//            log.error("Cant't read {} file at {}", APPLICATION_PROPERTIES_FILE, CLASSES_ROOT);
//            throw new DaoException("Cant't read %s file at %s".formatted(APPLICATION_PROPERTIES_FILE, CLASSES_ROOT));
//        }
//        log.info("Properties loaded from path: {}", applicationPropertiesPath);
//    }
//}
