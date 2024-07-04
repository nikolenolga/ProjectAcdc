package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.config.SessionCreater;

import java.util.Collection;

public class QuestRepository extends BaseRepository<Quest> {

    public QuestRepository(SessionCreater sessionCreater) {
        super(sessionCreater, Quest.class);
    }
}
