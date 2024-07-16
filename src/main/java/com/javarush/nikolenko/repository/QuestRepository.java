package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
}
