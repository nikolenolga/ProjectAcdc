package com.javarush.nikolenko.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.javarush.nikolenko.utils.Key.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ButtonServiceTest {

    private QuestEditService questEditServiceMock;
    private ButtonService buttonService;
    private HttpServletRequest httpServletRequest;
    private static List<String> allButtons;
    private String button;

    @BeforeAll
    static void beforeAll() {
        allButtons = new ArrayList<>(QUEST_EDIT_BUTTONS);
    }

    @BeforeEach
    void setUp() {
        questEditServiceMock = mock(QuestEditService.class);
        buttonService = new ButtonService(questEditServiceMock);
        httpServletRequest = mock(HttpServletRequest.class);
    }

    @Test
    void givenButtonLoadQuestImage_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_LOAD_QUEST_IMAGE;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).uploadQuestImage(any(HttpServletRequest.class));
    }

    @Test
    void givenButtonLoadQuestionImage_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_LOAD_QUESTION_IMAGE;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).uploadQuestionImage(any(HttpServletRequest.class));
    }

    @Test
    void givenButtonLoadAnswerImage_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_LOAD_ANSWER_IMAGE;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).uploadAnswerImage(any(HttpServletRequest.class));
    }

    @Test
    void givenButtonDeleteQuestion_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_DELETE_QUESTION;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).deleteQuestion(any(HttpServletRequest.class));
    }

    @Test
    void givenButtonDeleteAnswer_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_DELETE_ANSWER;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).deleteAnswer(any(HttpServletRequest.class));
    }

    @Test
    void givenButtonEditQuest_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_EDIT_QUEST;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).updateQuest(any(HttpServletRequest.class));
    }

    @Test
    void givenEditQuestion_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_EDIT_QUESTION;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).updateQuestion(any(HttpServletRequest.class));
    }

    @Test
    void givenButtonEditAnswer_whenExecuteGetOperation_thenVerifeyMethodExecuted() throws ServletException, IOException {
        //given
        button = BUTTON_EDIT_ANSWER;
        //when
        QuestEditOperation operation = buttonService.getOperation(button);
        operation.execute(httpServletRequest);
        //then
        verify(questEditServiceMock).updateAnswer(any(HttpServletRequest.class));
    }

    @AfterEach
    void tearDown() {
        if(button != null) {
            allButtons.remove(button);
        }
    }

    @AfterAll
    static void afterAll() {
        Assertions.assertTrue(allButtons.isEmpty());
    }

}