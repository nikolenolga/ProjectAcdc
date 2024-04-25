package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.utils.UrlHelper;
import org.apache.commons.lang3.ObjectUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class QuestService {
    private final QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public Optional<Quest> create(Quest quest) {
        if (ObjectUtils.allNotNull(quest.getName(), quest.getUserAuthorId(), quest.getFirstQuestionId(), quest.getDescription())) {
            questRepository.create(quest);
            return Optional.of(quest);
        }
        return Optional.empty();
    }

    public Optional<Quest> update(Quest quest) {
        if (ObjectUtils.allNotNull(quest.getName(), quest.getUserAuthorId(), quest.getFirstQuestionId(), quest.getDescription())) {
            questRepository.update(quest);
            return Optional.of(quest);
        }
        return Optional.empty();
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

    public Long getFirstQuestionId(long id) {
        return get(id).map(Quest::getFirstQuestionId).orElse(0L);
    }

    public Collection<Quest> getUserQuests(long id) {
        return questRepository.getUserQuests(id);
    }

    public String loadRules() {
        StringBuilder fileText = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(UrlHelper.RULES_FILE))) {

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return fileText.toString();
    }

    public 
}
