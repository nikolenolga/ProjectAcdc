package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

//SessionCreater is always single
@Slf4j
public class SessionCreater implements Closeable {
    private final SessionFactory sessionFactory;
    private final ThreadLocal<AtomicInteger> levelBox = new ThreadLocal<>();
    private final ThreadLocal<Session> sessionBox = new ThreadLocal<>();

    @SneakyThrows
    public SessionCreater(ApplicationProperties applicationProperties) {
        Configuration configuration = new Configuration();
        configuration.addProperties(applicationProperties);

        configuration.addAnnotatedClass(Answer.class)
                .addAnnotatedClass(Game.class)
                .addAnnotatedClass(Quest.class)
                .addAnnotatedClass(Question.class)
                .addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public Session getSession() {
        return sessionBox.get() == null || sessionBox.get().isOpen()
                ? sessionFactory.openSession()
                : sessionBox.get();
    }

    @Override
    public void close() throws IOException {
        sessionFactory.close();
    }

    public void beginTransactional() {
        if (levelBox.get() == null) {
            levelBox.set(new AtomicInteger(0));
        }
        AtomicInteger level = levelBox.get();
        if (level.getAndIncrement() == 0) {
            Session session = getSession();
            sessionBox.set(session);
            session.beginTransaction();
        }
        log(level.get(), "begin level: {}");
    }

    public void endTransactional() {
        AtomicInteger level = levelBox.get();
        Session session = sessionBox.get();
        log(level.get(), "end level: {}");
        if (level.decrementAndGet() == 0) {
            try {
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    private void log(int level, String message) {
        String simpleName = Thread.currentThread().getStackTrace()[4].toString();
        String formattedMessage = "%s%d from %s".formatted(message, level, simpleName);
        System.out.println("t".repeat(level) + formattedMessage);
        System.out.flush();
        log.debug(formattedMessage);
    }

}
