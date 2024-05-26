package com.javarush.nikolenko.lesson9Hibernate;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionCreaterTest {

    @Test
    void getSession() {
        SessionCreater sessionCreater = new SessionCreater();
        Session session = sessionCreater.getSession();
        System.out.println(session);
        Assertions.assertNotNull(session);
    }
}