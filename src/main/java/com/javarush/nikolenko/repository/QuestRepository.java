package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collection;

public class QuestRepository extends BaseRepository<Quest> {

    public QuestRepository(SessionCreater sessionCreater) {
        super(sessionCreater, Quest.class);
    }

    public int countAllQuests() {
        Session session = sessionCreater.getSession();
        String hql = "select count(*) from Quest ";
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.uniqueResult().intValue();
    }

    public boolean questWithCurrentNameExist(String name) {
        Session session = sessionCreater.getSession();
        String hql = "select u from Quest u where u.name = :currentName";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("currentName", name);
        query.setMaxResults(1);
        return !query.list().isEmpty();
    }
}
