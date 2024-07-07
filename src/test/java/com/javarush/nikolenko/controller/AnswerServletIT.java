package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.BaseControllerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.dto.GameTo;
import com.javarush.nikolenko.repository.GameRepository;
import com.javarush.nikolenko.service.GameService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnswerServletIT extends BaseControllerIT {

    private AnswerServlet answerServlet;
    private GameService gameService;
    private long gameId;
    private static final long questId = 1L;
    private static final long playerId = 1L;
    private GameTo gameTo;
    private final long answerIdFirst = 1L;
    private final long answerIdSecond = 2L;

    @BeforeEach
    void setUp() throws ServletException {
        answerServlet = new AnswerServlet();
        answerServlet.init(servletConfigMock);
        when(requestMock.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcherMock);

        gameService = NanoSpring.find(GameService.class);
        gameTo = gameService.initGame(playerId, questId);
        gameId = gameTo.getId();

        when(sessionMock.getAttribute(Key.GAME_ID)).thenReturn(gameId);
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerIdFirst));
    }

    @Test
    void whenOpenPage_thenGetSessionAttributeGameId() throws ServletException, IOException {
        //given //when
        answerServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getParameter(Key.ANSWER_ID);
    }

    @Test
    void whenOpenPage_thenGetRequestParameterAnswerId() throws ServletException, IOException {
        //given //when
        answerServlet.doGet(requestMock, responseMock);

        //then
        verify(sessionMock).getAttribute(Key.GAME_ID);
    }

    @Test
    void whenOpenPage_thenSeAttributeANSWER() throws ServletException, IOException {
        //given //when
        answerServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.ANSWER), any(AnswerTo.class));
    }

    @Test
    void whenOpenPage_thenForward() throws ServletException, IOException {
        //given //when
        answerServlet.doGet(requestMock, responseMock);

        //then
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void whenOpenPage_thenForwardJspIsAnswer() throws ServletException, IOException {
        //given //when
        answerServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getRequestDispatcher(UrlHelper.getJspPath(UrlHelper.ANSWER));
    }

    @Test
    void whencheckAnswer_thenAnswerGameStateEqualToGame() {
        //given //when
        AnswerTo answerTo = gameService.checkAnswer(answerIdFirst, gameId);
        GameRepository gameRepository = NanoSpring.find(GameRepository.class);
        GameState gameState = gameRepository.get(gameId).get().getGameState();

        //then
        assertEquals(gameState, answerTo.getGameState());
    }

    @Test
    void whencheckAnswer_thenFinalAnswerGameStateEqualToGame() {
        //given //when
        AnswerTo answerTo = gameService.checkAnswer(answerIdSecond, gameId);
        GameRepository gameRepository = NanoSpring.find(GameRepository.class);
        GameState gameState = gameRepository.get(gameId).get().getGameState();

        //then
        assertEquals(gameState, answerTo.getGameState());
    }

    @Test
    void whenOpenPage_thenVerifyGetSession() throws ServletException, IOException {
        //given //when
        answerServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getSession();
    }

    @Test
    void whenDoPost_thenVerifyGetSession() throws ServletException, IOException {
        //given //when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(requestMock).getSession(false);
    }

    @Test
    void whenDoPost_thenGetRequestParameterAnswerId() throws ServletException, IOException {
        //given //when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(sessionMock).getAttribute(Key.GAME_ID);
    }

    @Test
    void whenExitButtonPushed_thenSendRedirectToQuestion() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.BUTTON_NEXT)).thenReturn("submit");
        when(requestMock.getParameter(Key.BUTTON_RESTART)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_QUESTS)).thenReturn(null);
        // when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(responseMock).sendRedirect(UrlHelper.QUESTION);
    }

    @Test
    void whenRestartButtonPushed_thenSendRedirectToQuestion() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.BUTTON_NEXT)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_RESTART)).thenReturn("submit");
        when(requestMock.getParameter(Key.BUTTON_QUESTS)).thenReturn(null);
        // when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(responseMock).sendRedirect(UrlHelper.QUESTION);
    }

    @Test
    void whenRestartButtonPushed_thenGameRestarted() throws ServletException, IOException {
        //given
        AnswerTo answerTo = gameService.checkAnswer(answerIdSecond, gameId);
        when(requestMock.getParameter(Key.BUTTON_NEXT)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_RESTART)).thenReturn("submit");
        when(requestMock.getParameter(Key.BUTTON_QUESTS)).thenReturn(null);

        // when
        answerServlet.doGet(requestMock, responseMock);
        answerServlet.doPost(requestMock, responseMock);

        //then
        GameRepository gameRepository = NanoSpring.find(GameRepository.class);
        GameState gameState = gameRepository.get(gameId).get().getGameState();
        assertEquals(GameState.GAME, gameState);
    }

    @Test
    void whenQuestsButtonPushed_thenSendRedirectToQuests() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.BUTTON_NEXT)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_RESTART)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_QUESTS)).thenReturn("submit");
        // when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(responseMock).sendRedirect(UrlHelper.QUESTS);
    }

    @Test
    void whenQuestsButtonPushed_thenRemoveGameAttributeFromSession() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.BUTTON_NEXT)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_RESTART)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_QUESTS)).thenReturn("submit");
        // when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(sessionMock).removeAttribute(Key.GAME);
    }

    @Test
    void whenQuestsButtonPushed_thenRemoveGameIdAttributeFromSession() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.BUTTON_NEXT)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_RESTART)).thenReturn(null);
        when(requestMock.getParameter(Key.BUTTON_QUESTS)).thenReturn("submit");
        // when
        answerServlet.doPost(requestMock, responseMock);

        //then
        verify(sessionMock).removeAttribute(Key.GAME_ID);
    }
}