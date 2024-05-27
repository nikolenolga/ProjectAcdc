package com.javarush.nikolenko.lesson10HQL;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.lesson9Hibernate.SessionCreater;
import com.javarush.nikolenko.repository.Repository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class QuestDbDao implements Repository<Quest> {
    private SessionCreater sessionCreater;

    public QuestDbDao(SessionCreater sessionCreater) {
        this.sessionCreater = sessionCreater;
    }

    @Override
    public void create(Quest quest) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.persist(quest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void update(Quest quest) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.merge(quest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(Quest quest) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.remove(quest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<Quest> getAll() {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Quest> query = session.createQuery("SELECT q FROM Quest q", Quest.class);
                Collection<Quest> quests = query.getResultList();
                transaction.commit();
                return quests;
            } catch (Exception e) {
                transaction.rollback();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Optional<Quest> get(Long id) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                Quest quest = session.find(Quest.class, id);
                transaction.commit();
                return Optional.of(quest);
            } catch (Exception e) {
                transaction.rollback();
                return Optional.empty();
            }
        }
    }

    public Stream<Quest> find(Quest pattern) {

        return null;
    }
}
