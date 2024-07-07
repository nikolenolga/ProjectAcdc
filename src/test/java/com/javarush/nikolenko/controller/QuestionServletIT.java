package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.BaseControllerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.service.GameService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionServletIT extends BaseControllerIT {
    private QuestionServlet questionServlet;
    private GameService gameService;
    private long gameId;
    private static final long questId = 1L;
    private static final long playerId = 1L;
    private GameTo gameTo;

    @BeforeEach
    void setUp() throws ServletException {
        questionServlet = new QuestionServlet();
        questionServlet.init(servletConfigMock);
        when(requestMock.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcherMock);

        gameService = NanoSpring.find(GameService.class);
        gameTo = gameService.initGame(playerId, questId);
        gameId = gameTo.getId();

        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);
    }

    @Test
    void whenOpenPage_thenSeAttributeQUESTION() throws ServletException, IOException {
        //given //when
        questionServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.QUESTION), any(QuestionTo.class));
    }

    @Test
    void whenOpenPage_thenSeAttributeANSWERS() throws ServletException, IOException {
        //given //when
        questionServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.ANSWERS), any(Collection.class));
    }

    @Test
    void whenOpenPage_thenForward() throws ServletException, IOException {
        //given //when
        questionServlet.doGet(requestMock, responseMock);

        //then
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void whenOpenPage_thenRedirectToQuestionJsp() throws ServletException, IOException {
        //given //when
        questionServlet.doGet(requestMock, responseMock);

        //then
        String expectedPath = UrlHelper.getJspPath(UrlHelper.QUESTION);
        verify(requestMock).getRequestDispatcher(expectedPath);
    }

    @Test
    void whenOpenPage_thenGetCurrentQuestionFromGame() throws ServletException, IOException {
        //given //when
        QuestionTo questionTo = gameService.getCurrentQuestionWithAnswers(gameId);

        //then
        long expectedId = gameTo.getCurrentQuestionId();
        assertEquals(expectedId, questionTo.getId());
    }

    @Test
    void whenOpenPage_thenCurrentQuestionContainsAnswers() throws ServletException, IOException {
        //given //when
        QuestionTo questionTo = gameService.getCurrentQuestionWithAnswers(gameId);
        List<AnswerTo> answersTo = questionTo.getPossibleAnswers();

        //then
        assertFalse(answersTo.isEmpty());
    }

    @Test
    void givenOnlyNextQuestionLogicAnswer_whenChoose_thenSendRedirectToQuestion() throws ServletException, IOException {
        //given
        long answerId = 1L;
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);

        // when
        questionServlet.doPost(requestMock, responseMock);

        //then
        //AnswerTo(id=1, answerMessage=Принять вызов, finalMessage=null, gameState=GAME, nextQuestionId=2, questionId=1, image=answer-1)
        verify(responseMock).sendRedirect(UrlHelper.QUESTION);
    }

    @Test
    void givenFinalAnswerWithFinalMessage_whenChoose_thenSendRedirectToAnswerWithId() throws ServletException, IOException {
        //given
        long answerId = 2L;
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);

        // when
        questionServlet.doPost(requestMock, responseMock);

        //then
        //AnswerTo(id=2, answerMessage=Отклонить вызов, finalMessage=Ты отклонил вызов., gameState=LOSE, nextQuestionId=null, questionId=1, image=answer-2)
        verify(responseMock).sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.ANSWER, Key.ANSWER_ID, answerId));
    }

    @Test
    void whenChooseAnswer_thenSendRedirect() throws ServletException, IOException {
        //given
        long answerId = 2L;
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);

        // when
        questionServlet.doPost(requestMock, responseMock);

        //then
        verify(responseMock).sendRedirect(any(String.class));
    }

    @Test
    void whenChooseAnswer_thenVerifyGetSession() throws ServletException, IOException {
        //given
        long answerId = 2L;
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);

        // when
        questionServlet.doPost(requestMock, responseMock);

        //then
        verify(requestMock).getSession(false);
    }

    @Test
    void whenChooseAnswer_thenVerifyRequestGetAnswerIdKey() throws ServletException, IOException {
        //given
        long answerId = 2L;
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);

        // when
        questionServlet.doPost(requestMock, responseMock);

        //then
        verify(requestMock).getParameter(Key.ANSWER_ID);
    }

    @Test
    void whenChooseAnswer_thenVerifySessionGetGameIdKey() throws ServletException, IOException {
        //given
        long answerId = 2L;
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);

        // when
        questionServlet.doPost(requestMock, responseMock);

        //then
        verify(sessionMock).getAttribute(Key.GAME_ID);
    }
}