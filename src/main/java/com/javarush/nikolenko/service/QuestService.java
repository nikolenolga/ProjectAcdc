package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.repository.QuestRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class QuestService {
    private final QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public void create(Quest quest) {
        questRepository.create(quest);
    }

    public void update(Quest quest) {
        questRepository.update(quest);
    }

    public void delete(Quest quest) {
        questRepository.delete(quest);
    }

    public Collection<Quest> getAll() {
        return questRepository.getAll();
    }

    public Optional<Quest> get(long id) {
        return questRepository.get(id);
    }

    public long getCurrentQuestionId(long id) {
        Optional<Quest> optionalQuest = get(id);
        long result = optionalQuest.isPresent()
                ? optionalQuest.get().getId()
                : 0;
        return result;
    }




}
