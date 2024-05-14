package com.javarush.nikolenko.lesson7jdbc;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
    private  static Properties properties = new Properties();

    static {
        try {
            properties.load(ConfigUtil.class.getResourceAsStream(
                    "/application.properties"
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConfigUtil(){}

    public static String getValue(String key) {
        return properties.getProperty(key);
    }
}
