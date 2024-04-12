package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void create(Question question) {
        questionRepository.create(question);
    }

    public void update(Question question) {
        questionRepository.update(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public Collection<Question> getAll() {
        return questionRepository.getAll();
    }

    public Optional<Question> get(long id) {
        return questionRepository.get(id);
    }

    public Collection<Answer> getAnswersByQuestionId(long id) {
        Collection<Answer> answers;
        Optional<Question> optionalQuestion = get(id);
        answers = optionalQuestion.isPresent()
                ? optionalQuestion.get().getPossibleAnswers()
                : new ArrayList<>();
        return answers;
    }

}
