package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Quest;
import java.util.Collection;
import java.util.stream.Stream;

import static com.javarush.nikolenko.utils.Key.nullOrEquals;

public class QuestRepository extends BaseRepository<Quest> {
    public Collection<Quest> getUserQuests(long id) {
        return getAll()
                .stream()
                .filter(quest -> quest.getUserAuthorId() == id)
                .toList();
    }

    public Stream<Quest> find(Quest pattern) {
        return map.values()
                .stream()
                .filter(q -> nullOrEquals(pattern.getId(), q.getId()))
                .filter(q -> nullOrEquals(pattern.getName(), q.getName()))
                .filter(q -> nullOrEquals(pattern.getDescription(), q.getDescription()))
                .filter(q -> nullOrEquals(pattern.getFirstQuestionId(), q.getFirstQuestionId()))
                .filter(q -> nullOrEquals(pattern.getUserAuthorId(), q.getUserAuthorId()));
    }
}
