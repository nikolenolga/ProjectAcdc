package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.BaseControllerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.GameTo;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.GameService;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayServletIT extends BaseControllerIT {
    private final QuestService questService = NanoSpring.find(QuestService.class);
    private final GameService gameService = NanoSpring.find(GameService.class);
    private PlayServlet playServlet;
    private final long questId = 1L;
    private final long userId = 1L;
    private QuestTo questTo;

    @BeforeEach
    void setUp() throws ServletException {
        playServlet = new PlayServlet();
        playServlet.init(servletConfigMock);
        when(requestMock.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcherMock);
        questTo = questService.get(questId).orElse(QuestTo.builder().build());
    }

    @Test
    void whenOpenPage_thenVerifyRequestGetParameterQuestId() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));

        //when
        playServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getParameter(Key.QUEST_ID);
    }

    @Test
    void whenOpenPage_thenVerifyRequestSetAttributeQuest() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));

        //when
        playServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.QUEST), eq(questTo));
    }

    @Test
    void whenOpenPage_thenVerifyRequestSetAttributeQuestId() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));

        //when
        playServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.QUEST_ID), eq(questId));
    }

    @Test
    void whenOpenPage_thenForwardPathSetPlay() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));

        //when
        playServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getRequestDispatcher(UrlHelper.getJspPath(UrlHelper.PLAY));
    }

    @Test
    void whenOpenPage_thenForward() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));

        //when
        playServlet.doGet(requestMock, responseMock);

        //then
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void givenWrongQuestIdParameterInRequest_whenOpenPage_thenThrowException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("not_valid_parameter_value");

        //when //then
        assertThrows(Exception.class, () -> playServlet.doGet(requestMock, responseMock));
    }

    @Test
    void givenWrongQuestIdParameterInRequest_whenOpenPage_thenQuestException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("not_valid_parameter_value");

        //when //then
        assertThrows(QuestException.class, () -> playServlet.doGet(requestMock, responseMock));
    }

    @Test
    void givenNotValidQuestId_whenOpenPage_thenException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(0L));

        //when //then
        assertThrows(Exception.class, () -> playServlet.doGet(requestMock, responseMock));
    }

    @Test
    void givenNotValidQuestId_whenOpenPage_thenNoSuchElementException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(0L));

        //when //then
        assertThrows(NoSuchElementException.class, () -> playServlet.doGet(requestMock, responseMock));
    }

    @Test
    void givenValidDataInRequestAndButtonStartPushed_whenDoPost_thenVerifyAllAttributesGotAndSetted() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);

        //when
        playServlet.doPost(requestMock, responseMock);

        //then
        verify(requestMock).getParameter(Key.QUEST_ID);
        verify(requestMock).getParameter(Key.BUTTON_START);
        verify(requestMock).getSession(false);
        verify(sessionMock).getAttribute(Key.USER_ID);
        verify(sessionMock).setAttribute(eq(Key.GAME), any(GameTo.class));
        verify(sessionMock).setAttribute(eq(Key.GAME_ID), any(Long.class));
    }

    @Test
    void givenValidDataInRequestAndButtonStartPushed_whenDoPost_thenRedirectToQuestion() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);

        //when
        playServlet.doPost(requestMock, responseMock);

        //then
        verify(responseMock).sendRedirect(UrlHelper.QUESTION);
    }

    @Test
    void givenButtonStartNotPushed_whenDoPost_thenRedirectToPLAY() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn(null);

        //when
        playServlet.doPost(requestMock, responseMock);

        //then
        String expectedRedirectAdress = UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.PLAY, Key.QUEST_ID, questId);
        verify(responseMock).sendRedirect(expectedRedirectAdress);
    }

    @Test
    void givenWrongQuestIdParameterInRequest_whenDoPost_thenThrowException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("not_valid_parameter_value");

        //when //then
        assertThrows(Exception.class, () -> playServlet.doPost(requestMock, responseMock));
    }

    @Test
    void givenWrongQuestIdParameterInRequest_whenDoPost_thenQuestException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("not_valid_parameter_value");

        //when //then
        assertThrows(QuestException.class, () -> playServlet.doPost(requestMock, responseMock));
    }

    @Test
    void givenNotValidQuestId_whenDoPost_thenException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(0L));

        //when //then
        assertThrows(Exception.class, () -> playServlet.doPost(requestMock, responseMock));
    }

    @Test
    void givenWrongUserIdParameterInRequest_whenDoPost_thenThrowException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn("not_valid_parameter_value");

        //when //then
        assertThrows(Exception.class, () -> playServlet.doPost(requestMock, responseMock));
    }

    @Test
    void givenWrongUserIdParameterInRequest_whenDoPost_thenQuestException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn("not_valid_parameter_value");

        //when //then
        assertThrows(QuestException.class, () -> playServlet.doPost(requestMock, responseMock));
    }

    @Test
    void givenNotValidUserIdButValidQuestId_thenDoesNotThrowException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(0L);

        //when //then
        assertDoesNotThrow(() -> playServlet.doPost(requestMock, responseMock));
    }

    @Test
    void givenNotValidUserIdButValidQuestId_whenDoPost_thenDoesNotThrowException() throws ServletException, IOException {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.BUTTON_START)).thenReturn("submit");
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(0L);

        //when
        playServlet.doPost(requestMock, responseMock);

        //then
        verify(responseMock).sendRedirect(UrlHelper.QUESTION);
    }

}