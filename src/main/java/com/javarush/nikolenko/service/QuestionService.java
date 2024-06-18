package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Optional<Question> create(Question question) {
        if (question != null && ObjectUtils.allNotNull(question.getQuestionMessage())) {
            questionRepository.create(question);
            return Optional.of(question);
        }
        log.debug("Question creation failed, question - {}", question);
        return Optional.empty();
    }

    public Optional<Question> update(Question question) {
        if (question != null && ObjectUtils.allNotNull(question.getQuestionMessage())) {
            questionRepository.update(question);
            return Optional.of(question);
        }
        log.debug("Question updating failed, question - {}", question);
        return Optional.empty();
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
        return questionRepository.getAnswersByQuestionId(id);
    }

}
