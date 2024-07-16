package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.utils.LoggerConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_DRIVER;

@Slf4j
public class SessionCreater implements Closeable {
    public static final String TRY_END_LEVEL_SESSION = "<<< try end level: {} session={}";
    private final SessionFactory sessionFactory;
    private final ThreadLocal<AtomicInteger> levelBox = new ThreadLocal<>();
    private final ThreadLocal<Session> sessionBox = new ThreadLocal<>();

    @SneakyThrows
    public SessionCreater(ApplicationProperties applicationProperties) {
        log.info(LoggerConstants.START_CREATING_SESSION_FACTORY_WITH_CONFIGURATION);

        String driver = applicationProperties.getProperty(JAKARTA_JDBC_DRIVER);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error(LoggerConstants.DRIVER_CLASS_NOT_FOUND, driver, e);
            throw new QuestException(e);
        }

        Configuration configuration = new Configuration();
        configuration.addProperties(applicationProperties);

        configuration.addAnnotatedClass(Answer.class)
                .addAnnotatedClass(Game.class)
                .addAnnotatedClass(Quest.class)
                .addAnnotatedClass(Question.class)
                .addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();

        log.info(LoggerConstants.FINISH_CREATING_SESSION_FACTORY_WITH_CONFIGURATION);
    }

    public Session getSession() {
        return sessionBox.get() == null || !sessionBox.get().isOpen()
                ? sessionFactory.openSession()
                : sessionBox.get();
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
        log.info(LoggerConstants.START_LEVEL_SESSION, level.get(), sessionBox.get());
    }

    public void endTransactional() {
        AtomicInteger level = levelBox.get();
        Session session = sessionBox.get();
        log.info(TRY_END_LEVEL_SESSION, level.get(), session);

        if (level.decrementAndGet() == 0) {
            try {
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw new QuestException(e);
            } finally {
                sessionBox.remove();
                session.close();
            }
        }
        log.info(LoggerConstants.END_LEVEL_SUCCEED);
    }


    public void close() {
        sessionFactory.close();
    }

}
