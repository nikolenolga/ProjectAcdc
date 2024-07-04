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
        log.info("Start creating SessionFactory with configuration");

        Configuration configuration = new Configuration();
        configuration.addProperties(applicationProperties);

        configuration.addAnnotatedClass(Answer.class)
                .addAnnotatedClass(Game.class)
                .addAnnotatedClass(Quest.class)
                .addAnnotatedClass(Question.class)
                .addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();

        log.info("Finish creating SessionFactory with configuration");
    }

    public Session getSession() {
        return sessionBox.get() == null || sessionBox.get().isOpen()
                ? sessionFactory.openSession()
                : sessionBox.get();
    }

    @Override
    public void close(){
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
        log.info(">>> start level: {} session={}", level.get(), sessionBox.get());
    }

    public void endTransactional() {
        AtomicInteger level = levelBox.get();
        Session session = sessionBox.get();
        int levelBefourDecrement = level.get();

        if (level.decrementAndGet() == 0) {
            try {
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                log.warn("<<< on level : {} , session={} is rolledback", levelBefourDecrement, session);
                session.getTransaction().rollback();
                throw e;
            } finally {
                sessionBox.remove();
                session.close();
            }
        }
        log.info("<<< end level: {}, session={}", levelBefourDecrement, session);
    }

}
