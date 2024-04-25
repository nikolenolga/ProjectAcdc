package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.repository.AnswerRepository;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Optional<Answer> create(Answer answer) {
        if (ObjectUtils.allNotNull(answer.getAnswerMessage(), answer.getGameState(), answer.getFinalMessage())) {
            answerRepository.create(answer);
            return Optional.of(answer);
        }
        return Optional.empty();
    }

    public Optional<Answer> update(Answer answer) {
        if (ObjectUtils.allNotNull(answer.getAnswerMessage(), answer.getGameState(), answer.getFinalMessage())) {
            answerRepository.update(answer);
            return Optional.of(answer);
        }
        return Optional.empty();
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    public Collection<Answer> getAll() {
        return answerRepository.getAll();
    }

    public Optional<Answer> get(long id) {
        return answerRepository.get(id);
    }

    public boolean hasOnlyNextQuestionLogic(long id) {
        Optional<Answer> optionalAnswer = get(id);
        return optionalAnswer.isPresent() && optionalAnswer.get().hasOnlyNextQuestionLogic();
    }

    public boolean hasFinalMessage(long id) {
        Optional<Answer> optionalAnswer = get(id);
        return optionalAnswer.isPresent() && optionalAnswer.get().hasFinalMessage();
    }

    public boolean isFinal(long id) {
        Optional<Answer> optionalAnswer = get(id);
        return optionalAnswer.isPresent() && optionalAnswer.get().isFinal();
    }

    public Long getNextQuestionId(long id) {
        Optional<Answer> optionalAnswer = get(id);
        return optionalAnswer.map(Answer::getNextQuestionId).orElse(0L);
    }
}
