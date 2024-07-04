package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserRepository extends BaseRepository<User> {
    public UserRepository(SessionCreater sessionCreater) {
        super(sessionCreater, User.class);
    }

    public boolean userExist(String currentLogin) {
        Session session = sessionCreater.getSession();
        String hql = "select u from User u where u.login = :currentLogin";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("currentLogin", currentLogin);
        query.setMaxResults(1);
        return query.list().isEmpty();
    }

    public Optional<User> getUser(String currentLogin, String currentPassword) {
        Session session = sessionCreater.getSession();
        String hql = "select u from User u where u.login = :currentLogin and u.password = :currentPassword";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("currentLogin", currentLogin);
        query.setParameter("currentPassword", currentPassword);
        query.setMaxResults(1);
        return query.uniqueResultOptional();
    }

    public long countAllUsers() {
        Session session = sessionCreater.getSession();
        String hql = "select count(*) from User";
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.uniqueResult();
    }
}
