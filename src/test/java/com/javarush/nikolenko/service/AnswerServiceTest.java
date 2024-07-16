package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AnswerServiceTest {

    private AnswerRepository answerRepositoryMock;
    private QuestionRepository questionRepositoryMock;
    private AnswerService answerService;
    private Answer answer;
    private AnswerTo answerTo;

    @BeforeEach
    void setUp() {
        answer = Answer.builder()
                .id(3L)
                .gameState(GameState.GAME)
                .answerMessage("answer message")
                .finalMessage("final message")
                .build();
        answerTo = Dto.MAPPER.from(answer);
        answerRepositoryMock = mock(AnswerRepository.class);
        questionRepositoryMock = mock(QuestionRepository.class);
        answerService = new AnswerService(answerRepositoryMock, questionRepositoryMock);
    }

    @Test
    void givenTestAnswerAndDataForAupdate_whenUpdateAnswer_thenReturnAnswerWithUpdatedData() {
        //given
        long nextQuestionId = 7L;
        Question question = Question.builder().id(nextQuestionId).quest(Quest.builder().id(1L).build()).build();
        answer.setQuestion(question);
        when(answerRepositoryMock.get(answer.getId())).thenReturn(Optional.of(answer));
        when(questionRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(question));

        //when
        String answerMessage = "expected answer message";
        String finalMessage = "final answer message";
        GameState gameState = GameState.WIN;
        answerService.updateAnswer(answer.getId(), answerMessage, gameState, nextQuestionId, finalMessage);

        //then
        assertEquals(answerMessage, answer.getAnswerMessage());
        assertEquals(gameState, answer.getGameState());
        assertEquals(question, answer.getNextQuestion());
        assertEquals(finalMessage, answer.getFinalMessage());
    }

    @Test
    void givenTestAnswerTo_whenDelete_thenVerifeyAnswerRepositoryDeleteAnswer() {
        //when
        answerService.delete(answerTo);

        //then
        verify(answerRepositoryMock).delete(answer);
    }

    @Test
    void givenTestAnswerId_whenDelete_thenVerifeyAnswerRepositoryDeleteAnswer() {
        //given
        long answerId = answerTo.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(answer));

        //when
        answerService.delete(answerId);

        //then
        verify(answerRepositoryMock).delete(answerId);
    }
}