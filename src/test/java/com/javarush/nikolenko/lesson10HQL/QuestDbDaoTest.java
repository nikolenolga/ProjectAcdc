package com.javarush.nikolenko.lesson10HQL;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Stream;

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

    @Test
    @DisplayName("When find by id then get user id=1, role=ADMIN")
    void find() {
        //given
        Transaction transaction = session.beginTransaction();
        Quest pattern = Quest.builder().name("TestQuest").build();
        questDbDao.create(pattern);
        Long testQuestId = pattern.getId();
        //when
        Stream<Quest> questStream = questDbDao.find(pattern);
        questStream.forEach(System.out::println);
        questDbDao.delete(pattern);
        Stream<Quest> questStream2 = questDbDao.find(pattern);
        questStream2.forEach(System.out::println);
        transaction.commit();
    }


    @AfterEach
    void tearDown() throws IOException {
        session.close();
        sessionCreater.close();
    }
}