package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void updateQuestion(long questionId, String questionMessage) {
        questionRepository.get(questionId)
                .ifPresent(question -> question.setQuestionMessage(questionMessage));
    }

    public void delete(QuestionTo questionTo) {
        questionRepository.delete(Dto.MAPPER.from(questionTo));
    }

    public boolean delete(Long questionId) {
        return questionRepository.delete(questionId);
    }

    public void addNewAnswerToCreatedQuestion(long questionId, AnswerTo answerTo, long nextQuestionId) {
        Answer answer = Answer.builder()
                .answerMessage(answerTo.getAnswerMessage())
                .finalMessage(answerTo.getFinalMessage())
                .gameState(answerTo.getGameState())
                .build();
        answerRepository.create(answer).orElseThrow();

        Question question = questionRepository.get(questionId).orElseThrow();
        question.addPossibleAnswer(answer);
        setAnswersNextQuestionIfValid(nextQuestionId, question, answer);
    }

    private void setAnswersNextQuestionIfValid(long nextQuestionId, Question question, Answer answer) {
        Optional<Question> optionalNextQuestion;
        if (nextQuestionId != 0L && (optionalNextQuestion = questionRepository.get(nextQuestionId)).isPresent()) {
            Question nextQuestion = optionalNextQuestion.get();
            if (nextQuestion.getQuest().equals(question.getQuest())) {
                answer.setNextQuestion(nextQuestion);
            }
        }
    }
}
