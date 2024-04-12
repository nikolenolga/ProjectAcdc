package com.javarush.nikolenko.utils;

import com.javarush.nikolenko.exception.QuestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class RequestHelper {

    public static long getLongValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value != null && !value.isBlank()
                ? Long.parseLong(value)
                : 0L;
    }

    public static <T> T extractAttribute(HttpSession currentSession, String name, Class<T> clazz) throws QuestException {
        Object attribute = currentSession.getAttribute(name);
        if (attribute == null || clazz != attribute.getClass()) {
            currentSession.invalidate();
            throw new QuestException("Session is broken, try one more time");
        }
        return (T) attribute;
    }
//  исправить метод
//    public static <T> T extractAttribute(HttpServletRequest request, String name, Class<T> clazz) {
//        String parameter = request.getParameter(name);
//        if (parameter == null || clazz != parameter.getClass()) {
//            throw new QuestException("Request is broken, try one more time");
//        }
//        return (T) parameter;
//    }
}
