package com.javarush.khmelov.controller;

import com.javarush.khmelov.cmd.Command;
import com.javarush.khmelov.config.Winter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class HttpResolver {

    private final Map<String, Command> commandMap = new HashMap<>();

    @SneakyThrows
    public Command resolve(String name) {
        if (commandMap.get(name) == null) {
            String simpleName = convertSnakeStyleToCamelCase(name);
            String className = "com.javarush.khmelov.cmd." + simpleName;
            Class<?> aClass = Class.forName(className);
            Command command = (Command) Winter.find(aClass);
            commandMap.put(name, command);
        }
        return commandMap.get(name);
    }


    private static String convertSnakeStyleToCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : input.toCharArray()) {
            if (c == '-') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }
}
