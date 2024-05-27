package com.javarush.nikolenko.lesson10HQL;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.lesson9Hibernate.SessionCreater;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QuestDbDaoTest {
    private Session session;
    private SessionCreater sessionCreater;
    private QuestDbDao questDbDao;

    @BeforeEach
    void setUp() {
        sessionCreater = new SessionCreater();
        session = sessionCreater.getSession();
        questDbDao = new QuestDbDao(sessionCreater);
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }

    @Test
    void get() {
    }

    @Test
    void getQueryWithParam() {
        Transaction transaction = session.beginTransaction();
        Query<Quest> query = session.createNamedQuery("QUERY_MORE_ID", Quest.class);
        query.setParameter("id", 1L);
        query.getResultList().forEach(System.out::println);
        transaction.commit();
    }

    @AfterEach
    void tearDown() throws IOException {
        session.close();
        sessionCreater.close();
    }
}