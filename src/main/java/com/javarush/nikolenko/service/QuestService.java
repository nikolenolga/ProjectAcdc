package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.utils.Key;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class QuestService {
    private static final Logger log = LoggerFactory.getLogger(QuestService.class);
    private final QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
        log.info("QuestService created");
    }

    public Optional<Quest> create(Quest quest) {
        if (quest != null && ObjectUtils.allNotNull(quest.getName(), quest.getUserAuthorId(), quest.getFirstQuestionId(), quest.getDescription())) {
            questRepository.create(quest);
            return Optional.of(quest);
        }
        log.debug("Quest creation failed, quest - {}", quest);
        return Optional.empty();
    }

    public Optional<Quest> update(Quest quest) {
        if (quest != null && ObjectUtils.allNotNull(quest.getName(), quest.getUserAuthorId(), quest.getFirstQuestionId(), quest.getDescription())) {
            questRepository.update(quest);
            return Optional.of(quest);
        }
        log.debug("Quest updating failed, quest - {}", quest);
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

    public String loadTextFromFile(String sPath) {
        StringBuilder fileText = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(sPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileText.append(line).append("\r\n");
            }
        } catch (FileNotFoundException e) {
            log.error("Loading text failed, can't find file {}", sPath);
            throw new QuestException(Key.FILE_NOT_FOUND);
        } catch (IOException e) {
            log.error("Can't load file {}", sPath);
            throw new QuestException(Key.FILE_LOAD_ERROR);
        }
        return fileText.toString();
    }



}
