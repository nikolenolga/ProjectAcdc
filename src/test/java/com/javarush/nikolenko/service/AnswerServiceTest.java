package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.repository.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerServiceTest {
    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNullAnswer_whenCreate_thenReturnEmptyOptional() {
        //given
        Answer answer = null;

        //when
        Optional<Answer> actual = answerService.create(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenNullAnswerMessage_whenCreate_thenReturnEmptyOptional() {
        //given
        String answerMessage = null;
        Answer answer = new Answer(0L, answerMessage, "finalMessage", GameState.GAME, 1L, 1L);

        //when
        Optional<Answer> actual = answerService.create(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenNullGameState_whenCreate_thenReturnEmptyOptional() {
        //given
        GameState gameState = null;
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", gameState, 1L, 1L);

        //when
        Optional<Answer> actual = answerService.create(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenNullFinalMessage_whenCreate_thenReturnEmptyOptional() {
        //given
        String finalMessage = null;
        Answer answer = new Answer(0L, "answerMessage", finalMessage, GameState.GAME, 1L, 1L);

        //when
        Optional<Answer> actual = answerService.create(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenValidAnswer_whenCreate_thenReturnOptional() {
        //given
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);

        //when
        Optional<Answer> actual = answerService.create(answer);

        //then
        Optional<Answer> expected = Optional.of(answer);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
    }

    @Test
    void givenNullAnswer_whenUpdate_thenReturnEmptyOptional() {
        //given
        Answer answer = null;

        //when
        Optional<Answer> actual = answerService.update(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenNullAnswerMessage_whenUpdate_thenReturnEmptyOptional() {
        //given
        String answerMessage = null;
        Answer answer = new Answer(0L, answerMessage, "finalMessage", GameState.GAME, 3L, 1L);

        //when
        Optional<Answer> actual = answerService.update(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenNullGameState_whenUpdate_thenReturnEmptyOptional() {
        //given
        GameState gameState = null;
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", gameState, 3L, 1L);

        //when
        Optional<Answer> actual = answerService.update(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenNullFinalMessage_whenUpdate_thenReturnEmptyOptional() {
        //given
        String finalMessage = null;
        Answer answer = new Answer(0L, "answerMessage", finalMessage, GameState.GAME, 3L, 1L);

        //when
        Optional<Answer> actual = answerService.update(answer);

        //then
        Optional<Answer> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void givenValidAnswer_whenUpdate_thenReturn() {
        //given
        Answer expected = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);

        //when
        Optional<Answer> actual = answerService.update(expected);

        //then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void givenAnswer_whenAnswerServiceDelete_thenAnswerRepositoryDelete() {
        //given
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);
        //when
        answerService.delete(answer);
        //then
        verify(answerRepository, times(1)).delete(answer);
    }

    @Test
    void givenAnswersInRepository_whenGetAll_thenEqualCollections() {
        //given
        Collection<Answer> expectedAnswers = Stream.generate(() ->
                        new Answer(0L, "answerMessage", "finalMessage",
                                GameState.GAME, (long) (Math.random() * 35), 1L))
                        .limit(30)
                        .collect(Collectors.toList());
        when(answerRepository.getAll()).thenReturn(expectedAnswers);

        //when
        Collection<Answer> actualAnswers = answerService.getAll();
        //then
        assertEquals(expectedAnswers, actualAnswers);
    }

    @Test
    void givenValidAnswerIdInRepository_whenGet_thenPresentValue() {
        //given
        Answer expectedAnswer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);
        when(answerRepository.get(5L)).thenReturn(Optional.of(expectedAnswer));

        //when
        Optional<Answer> actualAnswer = answerService.get(5L);

        //then
        assertTrue(actualAnswer.isPresent());
        assertEquals(expectedAnswer, actualAnswer.get());
    }

    @Test
    void givenInValidAnswerIdInRepository_whenGet_thenEmptyValue() {
        //given
        when(answerRepository.get(2L)).thenReturn(Optional.empty());
        //when
        Optional<Answer> actualAnswer = answerService.get(2L);
        //then
        assertFalse(actualAnswer.isPresent());
    }

    @Test
    void givenValidAnswerWithFinalMessage_whenHasFinalMessage_thenCorrect() {
        //given
        String finalMessage = "finalMessage";
        Answer answer = new Answer(0L, "answerMessage", finalMessage, GameState.GAME, 3L, 1L);
        //when
        when(answerRepository.get(2L)).thenReturn(Optional.of(answer));
        //then
        assertTrue(answerService.hasFinalMessage(2L));
    }

    @Test
    void givenNullAnswer_whenHasFinalMessage_thenFalse() {
        //given //when
        when(answerRepository.get(2L)).thenReturn(Optional.empty());
        //then
        assertFalse(answerService.hasFinalMessage(2L));
    }

    @Test
    void givenValidAnswerWithEmptyFinalMessage_whenHasFinalMessage_thenFalse() {
        //given
        String finalMessage = "";
        Answer answer = new Answer(0L, "answerMessage", finalMessage, GameState.GAME, 3L, 1L);
        //when
        when(answerRepository.get(2L)).thenReturn(Optional.of(answer));
        //then
        assertFalse(answerService.hasFinalMessage(2L));
    }

    @Test
    void givenWinAnswer_whenIsFinal_thenTrue() {
        //given
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);
        //when
        when(answerRepository.get(1L)).thenReturn(Optional.of(answer));
        //then
        assertTrue(answerService.isFinal(1L));
    }

    @Test
    void givenLoseAnswer_whenIsFinal_thenTrue() {
        //given
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);
        //when
        when(answerRepository.get(1L)).thenReturn(Optional.of(answer));
        //then
        assertTrue(answerService.isFinal(1L));
    }

    @Test
    void givenGameAnswer_whenIsFinal_thenFalse() {
        //given
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, 3L, 1L);
        //when
        when(answerRepository.get(1L)).thenReturn(Optional.of(answer));
        //then
        assertFalse(answerService.isFinal(1L));
    }

    @Test
    void givenNullAnswer_whenIsFinal_thenFalse() {
        //given //when
        when(answerRepository.get(1L)).thenReturn(Optional.empty());
        //then
        assertFalse(answerService.isFinal(1L));
    }

    @Test
    void givenValidAnswerWithNextQuestion_whenGetNextQuestionId_thenGetValidId() {
        //given
        long expected  = 3L;
        Answer answer = new Answer(0L, "answerMessage", "finalMessage", GameState.GAME, expected, 1L);
        when(answerRepository.get(2L)).thenReturn(Optional.of(answer));
        //when
        long actual= answerService.getNextQuestionId(2L);
        //then
        assertEquals(expected, actual);
    }

    @Test
    void givenEmptyAnswer_whenGetNextQuestionId_thenGetZero() {
        //given
        long expected  = 0L;
        when(answerRepository.get(2L)).thenReturn(Optional.empty());
        //when
        long actual = answerService.getNextQuestionId(2L);
        //then
        assertEquals(expected, actual);
    }
}