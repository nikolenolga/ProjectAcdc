package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.repository.AnswerRepository;

import java.util.Collection;
import java.util.Optional;

public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void create(Answer answer) {
        answerRepository.create(answer);
    }

    public void update(Answer answer) {
        answerRepository.update(answer);
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
}
