package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameState;
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
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void updateAnswer(long answerId, String answerMessage, GameState gameState,
                             long nextQuestionId, String finalMessage) {
        Optional<Answer> optionalAnswer = answerRepository.get(answerId);
        Optional<Question> nextQuestion = questionRepository.get(nextQuestionId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (nextQuestion.isPresent() && answer.getQuestion().getQuest().equals(nextQuestion.get().getQuest())) {
                answer.setNextQuestion(nextQuestion.get());
            }
            answer.setFinalMessage(finalMessage);
            answer.setGameState(gameState);
            answer.setAnswerMessage(answerMessage);
        }
    }

    public void delete(AnswerTo answerTo) {
        answerRepository.delete(Dto.MAPPER.from(answerTo));
    }

    public boolean delete(Long answerId) {
        return answerRepository.delete(answerId);
    }
}
