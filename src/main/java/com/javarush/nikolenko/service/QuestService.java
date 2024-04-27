package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public String loadWebInfTextFile(String sPath) {
        final Path WEB_INF = Paths.get(URI.create(
                        Objects.requireNonNull(
                                QuestService.class.getResource("/")
                        ).toString()))
                .getParent();
        return loadTextFromFile(WEB_INF + sPath);
    }

    public String loadTextFromFile(String sPath) {
        StringBuilder fileText = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(sPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileText.append(line);
                fileText.append("\r\n");
            }
        } catch (FileNotFoundException e) {
            throw new QuestException(Key.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new QuestException(Key.FILE_LOAD_ERROR);
        }
        return fileText.toString();
    }

}
