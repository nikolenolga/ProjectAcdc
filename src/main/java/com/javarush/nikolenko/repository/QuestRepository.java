package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Quest;

import java.util.Collection;

public class QuestRepository extends BaseRepository<Quest> {
    public Collection<Quest> getUserQuests(long id) {
        return getAll()
                .stream()
                .filter(quest -> quest.getUserAuthorId() == id)
                .toList();
    }
}
