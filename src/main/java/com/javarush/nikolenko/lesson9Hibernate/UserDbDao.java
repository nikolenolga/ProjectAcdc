package com.javarush.nikolenko.lesson9Hibernate;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.repository.Repository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserDbDao implements Repository<User> {
    private SessionCreater sessionCreater;

    public UserDbDao(SessionCreater sessionCreater) {
        this.sessionCreater = sessionCreater;
    }

    @Override
    public void create(User user) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void update(User user) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.merge(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(User user) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.remove(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<User> getAll() {
        return List.of();
    }

    @Override
    public Optional<User> get(Long id) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                User user = session.find(User.class, id);
                transaction.commit();
                return Optional.of(user);
            } catch (Exception e) {
                transaction.rollback();
                return Optional.empty();
            }
        }
    }
}
