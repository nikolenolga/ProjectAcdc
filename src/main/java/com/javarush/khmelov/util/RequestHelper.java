package com.javarush.khmelov.util;

import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHelper {

    public static final Pattern CMD_URI_PATTERN = Pattern.compile(".*(/[a-z-]*)");

    private RequestHelper() {
    }

    public static String getCommand(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Matcher matcher = CMD_URI_PATTERN.matcher(uri);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new AppException("incorrect uri: " + uri);
        }
    }

    public static Long getId(HttpServletRequest req) {
        return getId(req, Key.ID);
    }

    public static Long getId(HttpServletRequest req, String key) {
        String id = req.getParameter(key);
        return id != null && !id.isBlank()
                ? Long.parseLong(id)
                : 0L;
    }

    public static Optional<User> getUser(HttpSession session) {
        Object user = session.getAttribute(Key.USER);
        return user != null
                ? Optional.of((User) user)
                : Optional.empty();
    }

    public static void setError(HttpServletRequest request, String errorMessage) {
        request.getSession().setAttribute(Key.ERROR_MESSAGE, errorMessage);
    }
}
