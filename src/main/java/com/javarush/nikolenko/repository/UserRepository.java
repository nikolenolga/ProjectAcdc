package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.config.SessionCreater;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.JpaRoot;
import jakarta.persistence.criteria.Predicate;

import java.util.*;

public class UserRepository extends BaseRepository<User> {
    public UserRepository(SessionCreater sessionCreater) {
        super(sessionCreater, User.class);
    }

    public boolean userWithCurrentLoginExist(String currentLogin) {
        Session session = sessionCreater.getSession();
        String hql = "select u from User u where u.login = :currentLogin";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("currentLogin", currentLogin);
        query.setMaxResults(1);
        return !query.list().isEmpty();
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

    @SneakyThrows
    public Optional<User> find(User pattern) {
        Session session = sessionCreater.getSession();
        var criteriaBuilder = session.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(User.class);
        JpaRoot<User> from = criteriaQuery.from(User.class);

        var predicates = new ArrayList<Predicate>();
        Map<String, Object> userFields = getNotNullFieldsMap(pattern);

        for (Map.Entry<String, Object> entry : userFields.entrySet()) {
            var predicate = criteriaBuilder.equal(from.get(entry.getKey()), entry.getValue());
            predicates.add(predicate);
        }
        criteriaQuery.select(from);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }

    private Map<String, Object> getNotNullFieldsMap(User pattern) {
        Map<String, Object> userFields = new HashMap<>();
        if(pattern.getId() != null) userFields.put("id", pattern.getId());
        if(pattern.getLogin() != null) userFields.put("login", pattern.getLogin());
        if(pattern.getName() != null) userFields.put("name", pattern.getName());
        if(pattern.getPassword() != null) userFields.put("password", pattern.getPassword());
        return userFields;
    }
}
