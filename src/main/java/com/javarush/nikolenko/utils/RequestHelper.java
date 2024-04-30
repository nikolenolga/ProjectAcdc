package com.javarush.nikolenko.utils;

import com.javarush.nikolenko.exception.QuestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class RequestHelper {
    public static final Path WEB_INF = Paths.get(URI.create(
                    Objects.requireNonNull(
                            RequestHelper.class.getResource("/")
                    ).toString()))
            .getParent();

    public static long getLongValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return getLong(value);
    }

    public static long getLongValue(HttpSession currentSession, String name) {
        try {
            Long value = (Long) currentSession.getAttribute(name);
            return value != null ? value : 0L;
        } catch (NumberFormatException e) {
            log.error("Current session does not contain Long attribute [{}], {}", name, e.getMessage());
            throw new QuestException(Key.CANT_EXT_REQUEST);
        }
    }

    public static long getLong(String value) {
        try {
            return value != null && !value.isBlank()
                    ? Long.parseLong(value)
                    : 0L;
        } catch (NumberFormatException e) {
            log.error("{}, while parsing [{}] attribute to Long", e.getMessage(), value);
            throw new QuestException(Key.CANT_EXT_REQUEST);
        }
    }

}
