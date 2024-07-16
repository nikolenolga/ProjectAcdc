package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import com.javarush.nikolenko.repository.UserRepository;
import com.javarush.nikolenko.utils.Key;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestEditServiceTest {
    private static final Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    QuestEditServiceTest.class.getResource("/")
            ).toString()));
    private static final String validPath = CLASSES_ROOT + File.separator + "quests" + File.separator + "quest-test.txt";

    private QuestRepository questRepositoryMock;
    private QuestionRepository questionRepositoryMock;
    private AnswerRepository answerRepositoryMock;
    private UserRepository userRepositoryMock;
    private ImageService imageServiceMock;
    private HttpServletRequest requestMock;
    private QuestService questServiceMock;
    private QuestionService questionServiceMock;
    private AnswerService answerServiceMock;
    private QuestEditService questEditService;
    private Quest savedQuest;
    private Question savedQuestion;
    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        questRepositoryMock = mock(QuestRepository.class);
        questionRepositoryMock = mock(QuestionRepository.class);
        answerRepositoryMock = mock(AnswerRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        imageServiceMock = mock(ImageService.class);
        questServiceMock = mock(QuestService.class);
        questionServiceMock = mock(QuestionService.class);
        answerServiceMock = mock(AnswerService.class);
        requestMock = mock(HttpServletRequest.class);

        savedQuest = Quest.builder().id(1L).build();
        savedQuestion = Question.builder().id(1L).build();
        savedAnswer = Answer.builder().id(1L).build();

        questEditService = new QuestEditService(questRepositoryMock,
                questionRepositoryMock, answerRepositoryMock, userRepositoryMock, questServiceMock, questionServiceMock,
                answerServiceMock, imageServiceMock);
    }

    @Test
    void whenUploadQuestImage_thenVerifyGet() throws ServletException, IOException {
        //given
        long questId = savedQuest.getId();
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedQuest));

        //when
        questEditService.uploadQuestImage(requestMock);

        //then
        verify(questRepositoryMock).get(questId);
    }

    @Test
    void whenUploadQuestImage_thenVerifyUploadImage() throws ServletException, IOException {
        //given
        long questId = savedQuest.getId();
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedQuest));

        //when
        questEditService.uploadQuestImage(requestMock);

        //then
        verify(imageServiceMock).uploadImage(requestMock, "quest-" + questId);
    }

    @Test
    void givenRequestDoesNotContainsKey_whenUploadQuestImage_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(null);
        when(questRepositoryMock.get(0L)).thenReturn(Optional.empty());

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadQuestImage(requestMock));
    }

    @Test
    void givenRequestDoesNotContainsKey_whenUploadQuestImage_thenThrowNoSuchElementException() {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(null);
        when(questRepositoryMock.get(0L)).thenReturn(Optional.empty());

        //when //then
        assertThrows(NoSuchElementException.class, () -> questEditService.uploadQuestImage(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenUploadQuestImage_thenThrowQuestException() {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.uploadQuestImage(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenUploadQuestImage_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadQuestImage(requestMock));
    }

    @Test
    void givenNotValidSavedQuestInRequest_whenUploadQuestImage_thenThrowException() {
        //given
        long questId = savedQuest.getId();
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(questRepositoryMock.get(questId)).thenReturn(Optional.empty());

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadQuestImage(requestMock));
    }

    @Test
    void whenUploadQuestionImage_thenVerifyGet() throws ServletException, IOException {
        //given
        long questionId = savedQuestion.getId();
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(String.valueOf(questionId));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));

        //when
        questEditService.uploadQuestionImage(requestMock);

        //then
        verify(questionRepositoryMock).get(questionId);
    }

    @Test
    void whenUploadQuestionImage_thenVerifyUploadImage() throws ServletException, IOException {
        //given
        long questionId = savedQuestion.getId();
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(String.valueOf(questionId));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.of(savedQuestion));

        //when
        questEditService.uploadQuestionImage(requestMock);

        //then
        verify(imageServiceMock).uploadImage(requestMock, "question-" + questionId);
    }

    @Test
    void givenRequestDoesNotContainsKey_whenUploadQuestionImage_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(null);
        when(questionRepositoryMock.get(0L)).thenReturn(Optional.empty());

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadQuestionImage(requestMock));
    }

    @Test
    void givenRequestDoesNotContainsKey_whenUploadQuestionImage_thenThrowNoSuchElementException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(null);
        when(questionRepositoryMock.get(0L)).thenReturn(Optional.empty());

        //when //then
        assertThrows(NoSuchElementException.class, () -> questEditService.uploadQuestionImage(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenUploadQuestionImage_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadQuestionImage(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenUploadQuestionImage_thenThrowQuestException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.uploadQuestionImage(requestMock));
    }

    @Test
    void givenNotValidSavedQuestionInRequest_whenUploadQuestionImage_thenThrowException() {
        //given
        long questionId = savedQuestion.getId();
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(String.valueOf(questionId));
        when(questionRepositoryMock.get(questionId)).thenReturn(Optional.empty());

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadQuestionImage(requestMock));
    }

    @Test
    void whenUploadAnswerImage_thenVerifyGet() throws ServletException, IOException {
        //given
        long answerId = savedAnswer.getId();
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(savedAnswer));

        //when
        questEditService.uploadAnswerImage(requestMock);

        //then
        verify(answerRepositoryMock).get(answerId);
    }

    @Test
    void whenUploadAnswerImage_thenVerifyUploadImage() throws ServletException, IOException {
        //given
        long answerId = savedAnswer.getId();
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(savedAnswer));

        //when
        questEditService.uploadAnswerImage(requestMock);

        //then
        verify(imageServiceMock).uploadImage(requestMock, "answer-" + answerId);
    }

    @Test
    void givenRequestDoesNotContainsKey_whenUploadAnswerImage_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(null);
        when(answerRepositoryMock.get(0L)).thenReturn(Optional.empty());

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadAnswerImage(requestMock));
    }

    @Test
    void givenRequestDoesNotContainsKey_whenUploadAnswerImage_thenThrowNoSuchElementException() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(null);
        when(answerRepositoryMock.get(0L)).thenReturn(Optional.empty());

        //when //then
        assertThrows(NoSuchElementException.class, () -> questEditService.uploadAnswerImage(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenUploadAnswerImage_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadAnswerImage(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenUploadAnswerImage_thenThrowQuestException() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.uploadAnswerImage(requestMock));
    }

    @Test
    void givenNotValidSavedAnswerInRequest_whenUploadAnswerImage_thenThrowException() {
        //given
        long answerId = savedAnswer.getId();
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.empty());

        //when //then
        assertThrows(Exception.class, () -> questEditService.uploadAnswerImage(requestMock));
    }

    @Test
    void whenDeleteQuestion_thenVerifyDelete() {
        //given
        long questionId = savedQuestion.getId();
        long questId = savedQuest.getId();
        savedQuest.addQuestion(savedQuestion);
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(String.valueOf(questionId));
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(questionRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(savedQuestion));
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(savedQuest));

        //when
        questEditService.deleteQuestion(requestMock);

        //then
        verify(questionRepositoryMock).delete(questionId);
    }

    @Test
    void givenRequestDoesNotContainsKey_whenDeleteQuestion_thenVerifyDelete0L() {
        //given
        long questionId = savedQuestion.getId();
        long questId = savedQuest.getId();
        savedQuest.addQuestion(savedQuestion);
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(null);
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(questionRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(savedQuestion));
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(savedQuest));

        //when
        questEditService.deleteQuestion(requestMock);

        // then
        verify(questionRepositoryMock).delete(0L);
    }

    @Test
    void givenRequestContainsNotValidValue_whenDeleteQuestion_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.deleteQuestion(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenDeleteQuestion_thenThrowQuestException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.deleteQuestion(requestMock));
    }

    @Test
    void whenDeleteAnswer_thenVerifyDelete() {
        //given
        long answerId = savedAnswer.getId();
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));

        //when
        questEditService.deleteAnswer(requestMock);

        //then
        verify(answerRepositoryMock).delete(answerId);
    }

    @Test
    void givenRequestDoesNotContainsKey_whenDeleteAnswer_thenVerifyDelete0L() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(null);

        //when
        questEditService.deleteAnswer(requestMock);

        // then
        verify(answerRepositoryMock).delete(0L);
    }

    @Test
    void givenRequestContainsNotValidValue_whenDeleteAnswer_thenThrowException() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.deleteAnswer(requestMock));
    }

    @Test
    void givenRequestContainsNotValidValue_whenDeleteAnswer_thenThrowQuestException() {
        //given
        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.deleteAnswer(requestMock));
    }

    @Test
    void whenUpdateQuest_thenVerifyUpdateQuest() {
        //given
        long questId = savedQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedQuestion.getId();
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.NAME)).thenReturn(name);
        when(requestMock.getParameter(Key.DESCRIPTION)).thenReturn(description);
        when(requestMock.getParameter(Key.FIRST_QUESTION_ID)).thenReturn(String.valueOf(firstQuestionId));

        //when
        questEditService.updateQuest(requestMock);

        //then
        verify(questServiceMock).updateQuest(questId, name, description, firstQuestionId);
    }

    @Test
    void givenRequestContainsNotValidQuestValue_whenUpdateQuest_thenThrowsException() {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.updateQuest(requestMock));
    }

    @Test
    void givenRequestContainsNotValidQuestValue_whenUpdateQuest_thenThrowsQuestException() {
        //given
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.updateQuest(requestMock));
    }

    @Test
    void givenRequestDoesNotContainQuestAndQuestionValue_whenUpdateQuest_thenThrowsException() {
        //given
        String name = "new name";
        String description = "new description";
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(null);
        when(requestMock.getParameter(Key.NAME)).thenReturn(name);
        when(requestMock.getParameter(Key.DESCRIPTION)).thenReturn(description);
        when(requestMock.getParameter(Key.FIRST_QUESTION_ID)).thenReturn(null);

        //when
        questEditService.updateQuest(requestMock);

        //then
        verify(questServiceMock).updateQuest(0L, name, description, 0L);
    }

    @Test
    void givenRequestContainsNotValidFirstQuestionIdValue_whenUpdateQuest_thenThrowsException() {
        //given
        long questId = savedQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedQuestion.getId();
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.NAME)).thenReturn(name);
        when(requestMock.getParameter(Key.DESCRIPTION)).thenReturn(description);
        when(requestMock.getParameter(Key.FIRST_QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.updateQuest(requestMock));
    }

    @Test
    void givenRequestContainsNotValidFirstQuestionIdValue_whenUpdateQuest_thenThrowsQuestException() {
        //given
        long questId = savedQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedQuestion.getId();
        when(requestMock.getParameter(Key.QUEST_ID)).thenReturn(String.valueOf(questId));
        when(requestMock.getParameter(Key.NAME)).thenReturn(name);
        when(requestMock.getParameter(Key.DESCRIPTION)).thenReturn(description);
        when(requestMock.getParameter(Key.FIRST_QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.updateQuest(requestMock));
    }

    @Test
    void whenUpdateQuestion_thenVerifyUpdateQuestion() {
        //given
        long questionId = savedQuest.getId();
        String questionMessage = "new name";

        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(String.valueOf(questionId));
        when(requestMock.getParameter(Key.QUESTION_MESSAGE)).thenReturn(questionMessage);

        //when
        questEditService.updateQuestion(requestMock);

        //then
        verify(questionServiceMock).updateQuestion(questionId, questionMessage);

    }

    @Test
    void givenRequestContainsNotValidQuestionValue_whenUpdateQuestion_thenThrowsException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(Exception.class, () -> questEditService.updateQuestion(requestMock));
    }

    @Test
    void givenRequestContainsNotValidQuestionValue_whenUpdateQuestion_thenThrowsQuestException() {
        //given
        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn("any_not_valid_value");

        //when //then
        assertThrows(QuestException.class, () -> questEditService.updateQuestion(requestMock));
    }

    @Test
    void givenRequestDoesNotContainQuestionValue_whenUpdateQuestion_thenVerifyQuestion0L() {
        //given
        String questionMessage = "new name";

        when(requestMock.getParameter(Key.QUESTION_ID)).thenReturn(null);
        when(requestMock.getParameter(Key.QUESTION_MESSAGE)).thenReturn(questionMessage);

        //when
        questEditService.updateQuestion(requestMock);

        //then
        verify(questionServiceMock).updateQuestion(0L, questionMessage);
    }

    @Test
    void whenUpdateAnswer_thenVerifyUpdateAnswer() {
        //given
        long answerId = savedAnswer.getId();
        String answerMessage = "new answerMessage";
        String gameState = "GAME";
        long nextQuestionId = savedQuestion.getId();
        String finalMessage = null;

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn(String.valueOf(nextQuestionId));
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when
        questEditService.updateAnswer(requestMock);

        //then
        verify(answerServiceMock).updateAnswer(answerId, answerMessage, GameState.GAME, nextQuestionId, finalMessage);

    }

    @Test
    void givenRequestDoesNotContainAnswerAndQuestionValue_whenUpdateAnswer_thenVerifyWith0LValues() {
        //given
        String answerMessage = "new answerMessage";
        String gameState = "WIN";
        String finalMessage = "new final message";

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(null);
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn(null);
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when
        questEditService.updateAnswer(requestMock);

        //then
        verify(answerServiceMock).updateAnswer(0L, answerMessage, GameState.WIN, 0L, finalMessage);

    }


    @Test
    void givenNotValidAnswerValue_whenUpdateAnswer_thenthrowException() {
        //given
        String answerMessage = "new answerMessage";
        String gameState = "GAME";
        long nextQuestionId = savedQuestion.getId();
        String finalMessage = null;

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn("any_not_valid_value");
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn(String.valueOf(nextQuestionId));
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when //then
        assertThrows(Exception.class, () -> questEditService.updateAnswer(requestMock));

    }

    @Test
    void givenNotValidAnswerValue_whenUpdateAnswer_thenthrowQuestException() {
        //given
        String answerMessage = "new answerMessage";
        String gameState = "GAME";
        long nextQuestionId = savedQuestion.getId();
        String finalMessage = null;

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn("any_not_valid_value");
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn(String.valueOf(nextQuestionId));
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when //then
        assertThrows(QuestException.class, () -> questEditService.updateAnswer(requestMock));

    }

    @Test
    void givenNotValidNextQuestionIdValue_whenUpdateAnswer_thenthrowQuestException() {
        //given
        long answerId = savedAnswer.getId();
        String answerMessage = "new answerMessage";
        String gameState = "GAME";
        String finalMessage = null;

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn("any_not_valid_value");
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when //then
        assertThrows(QuestException.class, () -> questEditService.updateAnswer(requestMock));

    }

    @Test
    void givenNotValidNextQuestionIdValue_whenUpdateAnswer_thenthrowException() {
        //given
        long answerId = savedAnswer.getId();
        String answerMessage = "new answerMessage";
        String gameState = "GAME";
        String finalMessage = null;

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn("any_not_valid_value");
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when //then
        assertThrows(Exception.class, () -> questEditService.updateAnswer(requestMock));

    }

    @Test
    void wheknUpdateAnswer_thenVerifyUpdateAnswer() {
        //given
        long answerId = savedAnswer.getId();
        String answerMessage = "new answerMessage";
        String gameState = "kkk";
        long nextQuestionId = savedQuestion.getId();
        String finalMessage = null;

        when(requestMock.getParameter(Key.ANSWER_ID)).thenReturn(String.valueOf(answerId));
        when(requestMock.getParameter(Key.ANSWER_MESSAGE)).thenReturn(answerMessage);
        when(requestMock.getParameter(Key.GAMESTATE)).thenReturn(gameState);
        when(requestMock.getParameter(Key.NEXT_QUESTION_ID)).thenReturn(String.valueOf(nextQuestionId));
        when(requestMock.getParameter(Key.FINAL_MESSAGE)).thenReturn(finalMessage);

        //when //then
        assertThrows(Exception.class, () -> questEditService.updateAnswer(requestMock));

    }

    @Test
    void givenWrongPath_whenLoadTextFromFile_thenThrowQuestException() {
        //given
        String wrongPath = "wrong path";
        //when //then
        assertThrows(QuestException.class, () -> questEditService.loadTextFromFile(wrongPath));
    }

    @Test
    void givenWrongPath_whenLoadTextFromFile_thenThrowException() {
        //given
        String wrongPath = "wrong path";
        //when //then
        assertThrows(Exception.class, () -> questEditService.loadTextFromFile(wrongPath));
    }

    @Test
    void givenValidPath_whenLoadTextFromFile_thenDoesNotThrowException() {
        //given //when //then
        assertDoesNotThrow(() -> questEditService.loadTextFromFile(validPath));
    }

}