package com.javarush.khmelov.util;

import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Parser {

    public String getCommand(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Matcher matcher = Pattern.compile(".*(/[a-z-]*)").matcher(uri);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new AppException("incorrect uri: " + uri);
        }
    }

    public Long getId(HttpServletRequest req) {
        return getId(req, Key.ID);
    }

    public Long getId(HttpServletRequest req, String key) {
        String id = req.getParameter(key);
        return id != null && !id.isBlank()
                ? Long.parseLong(id)
                : 0L;
    }

    public Long getId(HttpSession session) {
        Object user = session.getAttribute(Key.USER);
        return user != null
                ? ((User) user).getId()
                : 0L;
    }

    public Optional<User> getUser(HttpSession session) {
        Object user = session.getAttribute(Key.USER);
        return user != null
                ? Optional.of((User) user)
                : Optional.empty();
    }
}
