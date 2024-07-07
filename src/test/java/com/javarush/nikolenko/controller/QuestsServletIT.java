package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.BaseControllerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class QuestsServletIT extends BaseControllerIT {
    private QuestsServlet questsServlet;

    @BeforeEach
    void setUp() {
        questsServlet = new QuestsServlet();
        questsServlet.init(servletConfigMock);
        when(requestMock.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcherMock);
    }

    @Test
    void whenOpenPage_thenRedirectToQuestsJsp() throws ServletException, IOException {
        //when
        questsServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).getRequestDispatcher(UrlHelper.getJspPath(UrlHelper.QUESTS));
    }

    @Test
    void whenOpenPage_thenSetRequestAttributeQuests() throws ServletException, IOException {
        //when
        questsServlet.doGet(requestMock, responseMock);

        //then
        verify(requestMock).setAttribute(eq(Key.QUESTS), any(Collection.class));
    }

    @Test
    void whenOpenPage_thenForward() throws ServletException, IOException {
        //when
        questsServlet.doGet(requestMock, responseMock);

        //then
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void whenGetAllQuests_thenGetCollectionWithThreeQuests() {
        //given
        QuestService questService = NanoSpring.find(QuestService.class);

        //when
        Collection<QuestTo> quests = questService.getAll();

        //then
        assertEquals(3, quests.size());
    }

}