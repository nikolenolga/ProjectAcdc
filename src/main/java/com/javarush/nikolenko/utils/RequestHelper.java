package com.javarush.nikolenko.utils;

import com.javarush.nikolenko.config.Configuration;
import com.javarush.nikolenko.exception.QuestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
            throw new QuestException("Can't execute the request");
        }
    }

    public static long getLong(String value) {
        try {
            return value != null && !value.isBlank()
                    ? Long.parseLong(value)
                    : 0L;
        } catch (NumberFormatException e) {
            throw new QuestException("Can't execute the request");
        }
    }

    public static <T> T extractAttribute(HttpSession currentSession, String name, Class<T> clazz) throws QuestException {
        Object attribute = currentSession.getAttribute(name);
        if (attribute == null || clazz != attribute.getClass()) {
            currentSession.invalidate();
            throw new QuestException("Session is broken, try one more time");
        }
        return (T) attribute;
    }

}
