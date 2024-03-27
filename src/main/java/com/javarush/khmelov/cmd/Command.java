package com.javarush.khmelov.cmd;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Command {

    private static String convertCamelCaseToSnakeStyle(String string) {
        String snakeName = string.chars()
                .mapToObj(s -> String.valueOf((char) s))
                .flatMap(s -> s.matches("[A-Z]")
                        ? Stream.of("-", s)
                        : Stream.of(s))
                .collect(Collectors.joining())
                .toLowerCase();
        return snakeName.startsWith("-")
                ? snakeName.substring(1)
                : snakeName;
    }

    default String doGet(HttpServletRequest req) {
        return getJspPage();
    }

    default String getJspPage() {
        return "WEB-INF/%s.jsp".formatted(getPage());
    }

    default String doPost(HttpServletRequest req) throws ServletException, IOException {
        return getPage();
    }

    default String getPage() {
        String simpleName = this.getClass().getSimpleName();
        return convertCamelCaseToSnakeStyle(simpleName);
    }
}
