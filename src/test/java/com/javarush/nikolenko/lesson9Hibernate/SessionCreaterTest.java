package com.javarush.nikolenko.lesson9Hibernate;

import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SessionCreaterTest {

    @Test
    void getSession() {
        SessionCreater sessionCreater = new SessionCreater();
        Session session = sessionCreater.getSession();
        System.out.println(session);
        Assertions.assertNotNull(session);
    }
}