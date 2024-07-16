package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    private QuestionRepository questionRepositoryMock;
    private AnswerRepository answerRepositoryMock;
    private QuestionService questionService;
    private Answer answer;
    private Answer savedAnswer;
    private AnswerTo answerTo;
    private AnswerTo savedAnswerTo;
    private Question question;
    private Question savedQuestion;
    private QuestionTo savedQuestionTo;
    private QuestionTo questionTo;

    @BeforeEach
    void setUp() {
        questionRepositoryMock = mock(QuestionRepository.class);
        answerRepositoryMock = mock(AnswerRepository.class);
        questionService = new QuestionService(questionRepositoryMock, answerRepositoryMock);

        question = Question.builder()
                .questionMessage("First Message")
                .build();


        savedQuestion = Question.builder()
                .id(14L)
                .quest(Quest.builder().id(7L).build())
                .questionMessage("First Message")
                .build();

        answer = Answer.builder()
                .answerMessage("Answer message")
                .gameState(GameState.GAME)
                .nextQuestion(savedQuestion)
                .build();
        savedQuestion.addPossibleAnswer(answer);

        savedAnswer = Answer.builder()
                .id(15L)
                .answerMessage("Answer message")
                .gameState(GameState.GAME)
                .nextQuestion(savedQuestion)
                .build();
        savedQuestion.addPossibleAnswer(savedAnswer);

        questionTo = Dto.MAPPER.from(question);
        savedQuestionTo = Dto.MAPPER.from(savedQuestion);
        answerTo = Dto.MAPPER.from(answer);
        savedAnswerTo = Dto.MAPPER.from(savedAnswer);
    }

    @Test
    void givenValidData_whenUpdateQuestion_thenVerifyGet() {
        //given
        long questionId = savedQuestion.getId();
        String questionMessage = "new question message";
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));

        //when
        questionService.updateQuestion(questionId, questionMessage);

        // then
        verify(questionRepositoryMock).get(any(Long.class));
    }

    @Test
    void givenInValidData_whenUpdateQuestion_thenVerifyGet() {
        //given
        long questionId = 0L;
        String questionMessage = null;
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.empty());
        //when
        questionService.updateQuestion(questionId, questionMessage);

        // then
        verify(questionRepositoryMock).get(any(Long.class));
    }

    @Test
    void givenNewQuestionMessage_whenUpdateQuestion_thenReturnUpdatedQuestion() {
        //given
        long questionId = savedQuestion.getId();
        String questionMessage = "new question message";
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));

        //when
        questionService.updateQuestion(questionId, questionMessage);

        // then
        assertEquals(questionMessage, savedQuestion.getQuestionMessage());
    }

    @Test
    void qivenQuestionTo_whenDelete_thenVerifyDelete() {
        //given //when
        questionService.delete(savedQuestionTo);

        // then
        verify(questionRepositoryMock).delete(savedQuestion);
    }

    @Test
    void qivenQuestionTo_whenDeleteById_thenVerifyDelete() {
        //given
        long questionId = savedQuestion.getId();

        // when
        questionService.delete(questionId);

        // then
        verify(questionRepositoryMock).delete(questionId);
    }

    @Test
    void whenAddAnswerToCreatedQuestion_thenVerifyGet() {
        //given
        long questionId = 3L;
        long nextQuestionId = 5L;
        Quest savedQuest = Quest.builder().id(2L).build();
        savedQuest.addQuestion(savedQuestion);

        when(answerRepositoryMock.create(any(Answer.class))).thenReturn(Optional.of(savedAnswer));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));
        when(questionRepositoryMock.get(nextQuestionId)).thenReturn(Optional.of(savedQuestion));

        // when
        questionService.addNewAnswerToCreatedQuestion(questionId, answerTo, nextQuestionId);

        // then
        verify(questionRepositoryMock).get(questionId);
        verify(questionRepositoryMock).get(nextQuestionId);
    }

    @Test
    void whenAddAnswerToCreatedQuestion_thenVerifyCreate() {
        //given
        when(answerRepositoryMock.create(any(Answer.class))).thenReturn(Optional.of(savedAnswer));
        when(questionRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(savedQuestion));
        when(answerRepositoryMock.create(answer)).thenReturn(Optional.of(savedAnswer));
        Quest savedQuest = Quest.builder().id(2L).build();
        savedQuest.addQuestion(savedQuestion);

        // when
        questionService.addNewAnswerToCreatedQuestion(8L, answerTo, 5L);

        // then
        verify(answerRepositoryMock).create(any(Answer.class));
    }

    @Test
    void whenCantCreateAnswer_thenThrowException() {
        // when
        when(answerRepositoryMock.create(answer)).thenReturn(Optional.empty());
        // then
        assertThrows(Exception.class, () -> questionService.addNewAnswerToCreatedQuestion(8L, answerTo, 5L));
    }

    @Test
    void whenAddAnswerToCreatedQuestion_thenQuestionContainsAnswer() {
        //given
        long questionId = savedQuestion.getId();
        long nextQuestionId = savedQuestion.getId();
        Quest savedQuest = Quest.builder().id(2L).build();
        savedQuest.addQuestion(savedQuestion);

        when(answerRepositoryMock.create(any(Answer.class))).thenReturn(Optional.of(savedAnswer));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));
        when(questionRepositoryMock.get(nextQuestionId)).thenReturn(Optional.of(savedQuestion));

        // when
        questionService.addNewAnswerToCreatedQuestion(questionId, answerTo, nextQuestionId);
        List<Answer> answers = savedQuestion.getPossibleAnswers();

        // then
        assertTrue(answers.contains(savedAnswer));
    }

    @Test
    void whenAddAnswerToCreatedQuestion_thenAnswerNextQuestionSetted() {
        //given
        long questionId = 8L;
        long nextQuestionId = savedQuestion.getId();
        Quest savedQuest = Quest.builder().id(2L).build();
        savedQuest.addQuestion(savedQuestion);
        when(answerRepositoryMock.create(any(Answer.class))).thenReturn(Optional.of(savedAnswer));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));
        when(questionRepositoryMock.get(nextQuestionId)).thenReturn(Optional.of(savedQuestion));

        // when
        questionService.addNewAnswerToCreatedQuestion(questionId, answerTo, nextQuestionId);

        // then
        assertEquals(savedAnswer.getNextQuestion(), savedQuestion);
    }

    @Test
    void whenAddAnswerToCreatedQuestion_thenAnswerQuestionSetted() {
        //given
        long questionId = savedQuestion.getId();
        long nextQuestionId = 8L;
        Quest savedQuest = Quest.builder().id(2L).build();
        savedQuest.addQuestion(savedQuestion);

        when(answerRepositoryMock.create(any(Answer.class))).thenReturn(Optional.of(savedAnswer));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));
        when(questionRepositoryMock.get(nextQuestionId)).thenReturn(Optional.of(savedQuestion));

        // when
        questionService.addNewAnswerToCreatedQuestion(questionId, answerTo, nextQuestionId);

        // then
        assertEquals(savedAnswer.getQuestion(), savedQuestion);
    }
}