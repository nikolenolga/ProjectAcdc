package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import com.javarush.nikolenko.repository.UserRepository;
import com.javarush.nikolenko.utils.LoggerConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
public class QuestService {
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;

    public Optional<QuestTo> create(QuestTo questTo) {
        Quest quest = Dto.MAPPER.from(questTo);
        if (validateQuest(quest)) {
            return questRepository.create(quest).map(Dto.MAPPER::from);
        }
        return Optional.empty();
    }

    public Optional<QuestTo> createQuest(long authorId, String name, String description) {
        User author = userRepository.get(authorId).orElseThrow();
        Question firstQuestion = Question.builder().build();
        questionRepository.create(firstQuestion);

        Quest quest = Quest.builder()
                .name(name)
                .description(description)
                .firstQuestion(firstQuestion)
                .author(author)
                .build();
        quest.addQuestion(firstQuestion);

        if (validateQuest(quest)) {
            return questRepository.create(quest).map(Dto.MAPPER::from);
        }
        return Optional.empty();
    }

    public void delete(QuestTo questTo) {
        questRepository.delete(Dto.MAPPER.from(questTo));
    }

    public boolean delete(long questId) {
        return questRepository.delete(questId);
    }

    public Collection<QuestTo> getAll() {
        return questRepository.getAll()
                .stream()
                .map(Dto.MAPPER::from)
                .toList();
    }

    public QuestTo getQuestWithQuestions(long questId) {
        QuestTo questTo = questRepository.get(questId).map(Dto.MAPPER::from).orElseThrow();
        questTo.getQuestions();
        return questTo;
    }

    public Optional<QuestTo> get(long id) {
        return questRepository.get(id).map(Dto.MAPPER::from);
    }

    public void updateQuest(long questId, String name, String description, long firstQuestionId) {
        Optional<Quest> optionalQuest = questRepository.get(questId);
        Optional<Question> optionalQuestion = questionRepository.get(firstQuestionId);
        if (optionalQuest.isPresent() && optionalQuestion.isPresent()) {
            Quest quest = optionalQuest.get();
            quest.setName(name);
            quest.setDescription(description);
            quest.setFirstQuestion(optionalQuestion.get());
            questRepository.update(quest);
        }
    }

    public void addNewQuestionToCreatedQuest(long questId, String questionMessage) {
        Quest quest = questRepository.get(questId).orElseThrow();
        if (questionMessage == null || questionMessage.isBlank()) {
            log.error(LoggerConstants.QUESTION_MESSAGE_IS_NULL_OR_EMPTY);
            throw new QuestException("QuestionMessage is null or empty");
        }

        Question question = Question.builder()
                .questionMessage(questionMessage)
                .build();
        questionRepository.create(question);
        quest.addQuestion(question);
    }

    private boolean validateQuest(Quest quest) {
        return quest != null && ObjectUtils.allNotNull(quest.getName());
    }

    public int countAllQuests() {
        return questRepository.countAllQuests();
    }
}
