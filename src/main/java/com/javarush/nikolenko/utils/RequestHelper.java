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
public final class RequestHelper {
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
            return (value == null) ? 0L : value;
        } catch (NumberFormatException | ClassCastException e) {
            log.error(LoggerConstants.CURRENT_SESSION_DOES_NOT_CONTAIN_LONG_ATTRIBUTE, name, e.getMessage());
            throw new QuestException("Can't find long value: %s in session, exception: %s".formatted(name, e));
        }
    }

    public static long getLong(String value) {
        try {
            return (value == null) ? 0L : Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error(LoggerConstants.WHILE_PARSING_ATTRIBUTE_TO_LONG, e.getMessage(), value);
            throw new QuestException("Can't find long value: %s, exception: %s".formatted(value, e));
        }
    }
}
