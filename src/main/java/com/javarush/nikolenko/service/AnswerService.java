package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
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
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

//    public Optional<AnswerTo> create(AnswerTo answerTo) {
//        if (validateAnswer(answerTo)) {
//            return answerRepository.create(Dto.MAPPER.from(answerTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("Answer can't be created in repository, answer - {}", answerTo);
//        return Optional.empty();
//    }
//
//    public Optional<AnswerTo> update(AnswerTo answerTo) {
//        if (validateAnswer(answerTo)) {
//            return answerRepository.update(Dto.MAPPER.from(answerTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("Answer can't be updated in repository, answer - {}", answerTo);
//        return Optional.empty();
//    }

    public void updateAnswer(long answerId, String answerMessage, GameState gameState,
                             long nextQuestionId, String finalMessage) {
        Optional<Answer> optionalAnswer = answerRepository.get(answerId);
        Optional<Question> nextQuestion = questionRepository.get(nextQuestionId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            nextQuestion.ifPresent(answer::setNextQuestion);
            answer.setNextQuestion(nextQuestion.orElse(null));
            answer.setFinalMessage(finalMessage);
            answer.setGameState(gameState);
        }
    }

    public void delete(AnswerTo answerTo) {
        answerRepository.delete(Dto.MAPPER.from(answerTo));
    }

    public void delete(Long answerId) {
        answerRepository.get(answerId).ifPresent(answerRepository::delete);
    }

//    public Collection<AnswerTo> getAll() {
//        return answerRepository.getAll().stream().map(Dto.MAPPER::from).toList();
//    }
//
//    public Optional<AnswerTo> get(long id) {
//        return answerRepository.get(id).map(Dto.MAPPER::from);
//    }
//
//    private boolean validateAnswer(AnswerTo answerTo) {
//        return answerTo != null && ObjectUtils.allNotNull(answerTo.getAnswerMessage(), answerTo.getGameState(), answerTo.getFinalMessage());
//    }
//
//    public boolean hasFinalMessage(long id) {
//        Optional<Answer> optionalAnswer = answerRepository.get(id);
//        return optionalAnswer.isPresent() && optionalAnswer.get().hasFinalMessage();
//    }
//
//    public boolean isFinal(long id) {
//        Optional<Answer> optionalAnswer = answerRepository.get(id);
//        return optionalAnswer.isPresent() && optionalAnswer.get().isFinal();
//    }
//
//    public QuestionTo getNextQuestion(long id) {
//        Optional<Answer> optionalAnswer = answerRepository.get(id);
//        return optionalAnswer.map(Answer::getNextQuestion).map(Dto.MAPPER::from).orElse(null);
//    }
}
