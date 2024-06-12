package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.lesson9Hibernate.UserDbDao;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

//SessionCreater всегда один
public class SessionCreater implements Closeable {
    private final SessionFactory sessionFactory;

    @SneakyThrows
    public SessionCreater() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Answer.class)
                .addAnnotatedClass(Game.class)
                .addAnnotatedClass(Quest.class)
                .addAnnotatedClass(Question.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserInfo.class)
                .buildSessionFactory();

        sessionFactory = configuration.buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void close() throws IOException {
        sessionFactory.close();
    }
}
