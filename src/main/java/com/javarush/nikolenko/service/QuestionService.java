package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AnswerRepository answerRepository;

//    public Optional<QuestionTo> create(QuestionTo questionTo) {
//        if (validateQuestion(questionTo)) {
//            return questionRepository.create(Dto.MAPPER.from(questionTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("Question creation failed, question - {}", questionTo);
//        return Optional.empty();
//    }
//
//    public Optional<QuestionTo> update(QuestionTo questionTo) {
//        if (validateQuestion(questionTo)) {
//            return questionRepository.update(Dto.MAPPER.from(questionTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("Question updating failed, question - {}", questionTo);
//        return Optional.empty();
//    }

    public void updateQuestion(long questionId, String questionMessage) {
        questionRepository.get(questionId)
                .ifPresent(question -> question.setQuestionMessage(questionMessage));
    }

    public void delete(QuestionTo questionTo) {
        questionRepository.delete(Dto.MAPPER.from(questionTo));
    }

    public void delete(Long questionId) {
        questionRepository.get(questionId).ifPresent(questionRepository::delete);
    }

//    public Collection<QuestionTo> getAll() {
//        return questionRepository.getAll().stream().map(Dto.MAPPER::from).toList();
//    }
//
//    public Optional<QuestionTo> get(long id) {
//        return questionRepository.get(id).map(Dto.MAPPER::from);
//    }
//
//    public Collection<AnswerTo> getAnswersByQuestionId(long id) {
//        return questionRepository.getAnswersByQuestionId(id).stream().map(Dto.MAPPER::from).toList();
//    }
//
//    public void addAnswer(long questionId, long answerId) {
//        Optional<Question> optionalQuestion = questionRepository.get(questionId);
//        Optional<Answer> optionalAnswer = answerRepository.get(answerId);
//        if (optionalQuestion.isPresent() && optionalAnswer.isPresent()) {
//            Question question = optionalQuestion.get();
//            question.addPossibleAnswer(optionalAnswer.get());
//            log.info("Answer {} added to question {}", answerId, question.getId());
//        }
//    }

//    public void addNewAnswerToCreatedQuestion(long questionId, AnswerTo answerTo, long nextQuestionId) {
//        Answer answer = Dto.MAPPER.from(answerTo);
//        answerRepository.create(answer).orElseThrow();
//
//        questionRepository.get(nextQuestionId).ifPresent(answer::setNextQuestion);
//        questionRepository.get(questionId).ifPresent(question -> question.addPossibleAnswer(answer));
//    }

    public void addNewAnswerToCreatedQuestion(long questionId, AnswerTo answerTo, long nextQuestionId) {
        Answer answer = Answer.builder()
                .answerMessage(answerTo.getAnswerMessage())
                .finalMessage(answerTo.getFinalMessage())
                .gameState(answerTo.getGameState())
                .build();
        answerRepository.create(answer).orElseThrow();

        Question question = questionRepository.get(questionId).orElseThrow();
        question.addPossibleAnswer(answer);

        Optional<Question> optionalNextQuestion;
        if(nextQuestionId != 0L && (optionalNextQuestion = questionRepository.get(nextQuestionId)).isPresent()) {
            Question nextQuestion = optionalNextQuestion.get();
            if(nextQuestion.getQuest().equals(question.getQuest())) {
                answer.setNextQuestion(nextQuestion);
            }
        }


    }

//    public void addNewAnswerToQuestion(AnswerTo answerTo) {
//        Answer answer = Dto.MAPPER.from(answerTo);
//        answerRepository.create(answer).orElseThrow();
//    }

//    private boolean validateQuestion(QuestionTo questionTo) {
//        return questionTo != null && ObjectUtils.allNotNull(questionTo.getQuestionMessage());
//    }

}
