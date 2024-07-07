package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.BaseControllerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserQuestsServletIT  extends BaseControllerIT {
    private final UserService userService = NanoSpring.find(UserService.class);
    private UserQuestsServlet userQuestsServlet;
    private final long userId = 1L;

    @BeforeEach
    void setUp() throws ServletException {
        userQuestsServlet = new UserQuestsServlet();
        userQuestsServlet.init(servletConfigMock);
        when(requestMock.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcherMock);
    }

    @Test
    void whenOpenPage_thenVerifyAllAttribetesGotAndSet() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);

        //when
        userQuestsServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getSession();
        verify(sessionMock).getAttribute(Key.USER_ID);
        verify(requestMock).setAttribute(eq(Key.QUESTS), any(Collection.class));
    }

    @Test
    void givenValidDataInRequest_whenOpenPage_thenForwardJspPathSetUserQuests() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);

        //when
        userQuestsServlet.doGet(requestMock, responseMock);

        //then
        String expectedPath = UrlHelper.getJspPath(UrlHelper.USER_QUESTS);
        verify(requestMock).getRequestDispatcher(expectedPath);
    }


    @Test
    void givenValidDataInRequest_whenOpenPage_thenForward() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);

        //when
        userQuestsServlet.doGet(requestMock, responseMock);

        //then
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void givenValidUser_whenOpenPage_thenUserQuestsSetInRequestAttribute() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(userId);
        Collection<QuestTo> quests = userService.getUserQuests(userId);

        //when
        userQuestsServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.QUESTS), eq(quests));
    }

    @Test
    void givenNotValidUser_whenOpenPage_thenDoesNotThrowException() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(0L);

        //when
        assertDoesNotThrow(() -> userQuestsServlet.doGet(requestMock, responseMock));
    }

    @Test
    void givenNotValidUser_whenOpenPage_thenForwardToUserQuestsJsp() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn(0L);

        //when
        userQuestsServlet.doGet(requestMock, responseMock);

        //then
        String expectedPath = UrlHelper.getJspPath(UrlHelper.USER_QUESTS);
        verify(requestMock).getRequestDispatcher(expectedPath);
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void givenNotUserIdAttributeInSession_whenOpenPage_thenThrowException() throws ServletException, IOException {
        //given
        when(sessionMock.getAttribute(Key.USER_ID)).thenReturn("not_valid_attribute_value");

        //when
        assertThrows(Exception.class, () -> userQuestsServlet.doGet(requestMock, responseMock));
    }

}