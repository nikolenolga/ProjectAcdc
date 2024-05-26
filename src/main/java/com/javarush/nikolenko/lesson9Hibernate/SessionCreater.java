package com.javarush.nikolenko.lesson9Hibernate;

import com.javarush.nikolenko.entity.User;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

//SessionCreater всегда один
public class SessionCreater implements Closeable {
    private final SessionFactory sessionFactory;

    @SneakyThrows
    public SessionCreater() {
        Configuration configuration = new Configuration();

        sessionFactory = configuration.configure()
                        .addAnnotatedClass(User.class)
                        .buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void close() throws IOException {
        sessionFactory.close();
    }

    public static void main(String[] args) throws IOException {
        SessionCreater sessionCreater = new SessionCreater();
        UserDbDao dbDao = new UserDbDao(sessionCreater);

        Optional<User> optionalUser = dbDao.get(1L);
        optionalUser.ifPresent(System.out::println);
    }
}
