package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import com.javarush.nikolenko.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestServiceTest {
    private QuestRepository questRepositoryMock;
    private QuestionRepository questionRepositoryMock;
    private UserRepository userRepositoryMock;
    private QuestService questService;
    private Quest testQuest;
    private Question testQuestion;
    private QuestTo testQuestTo;
    private QuestionTo testQuestionTo;
    private Quest savedTestQuest;
    private Question savedTestQuestion;
    private QuestTo savedTestQuestTo;
    private QuestionTo savedTestQuestionTo;
    private UserTo savedTestUserTo;
    private User savedTestUser;


    @BeforeEach
    void setUp() {
        questRepositoryMock = mock(QuestRepository.class);
        questionRepositoryMock = mock(QuestionRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        questService = new QuestService(userRepositoryMock, questRepositoryMock, questionRepositoryMock);

        testQuest = Quest.builder()
                .name("Test Quest")
                .description("Test Description")
                .build();

        testQuestion = Question.builder()
                .quest(testQuest)
                .questionMessage("First Message")
                .build();
        testQuest.setFirstQuestion(testQuestion);

        testQuestTo = Dto.MAPPER.from(testQuest);
        testQuestionTo = Dto.MAPPER.from(testQuestion);

        savedTestQuest = Quest.builder()
                .id(10L)
                .name("Test Quest")
                .description("Test Description")
                .build();

        savedTestQuestion = Question.builder()
                .id(18L)
                .quest(testQuest)
                .questionMessage("First Message")
                .build();
        savedTestQuest.setFirstQuestion(testQuestion);

        savedTestQuestTo = Dto.MAPPER.from(savedTestQuest);
        savedTestQuestionTo = Dto.MAPPER.from(savedTestQuestion);

        savedTestUserTo = UserTo.builder()
                .id(1L)
                .login("TestLogin")
                .password("TestPassword")
                .name("TestName")
                .build();
        savedTestUser = Dto.MAPPER.from(savedTestUserTo);
    }

    @Test
    void givenNotValidQuestData_whenCreate_thenReturnFalse(){
        //given
        String name = null;
        String description = null;
        when(userRepositoryMock.get(savedTestUserTo.getId())).thenReturn(Optional.ofNullable(savedTestUser));
        //when
        Optional<QuestTo> actual = questService.createQuest(savedTestUserTo.getId(), name, description);
        //given
        assertFalse(actual.isPresent());
    }



    @Test
    void givenValidQuestTo_whenCreate_thenReturnTrue(){
        //given
        when(questRepositoryMock.create(testQuest)).thenReturn(Optional.ofNullable(savedTestQuest));
        //when
        Optional<QuestTo> questTo = questService.create(testQuestTo);
        //given
        assertTrue(questTo.isPresent());
    }

    @Test
    void givenValidQuestTo_whenCreate_thenVerifyCreate(){
        //given
        when(questRepositoryMock.create(any(Quest.class))).thenReturn(Optional.ofNullable(savedTestQuest));
        //when
        questService.create(testQuestTo);
        //given
        verify(questRepositoryMock).create(testQuest);
    }

    @Test
    void givenQuestTo_whenCantCreate_thenReturnFalse(){
        //given
        when(questRepositoryMock.create(any(Quest.class))).thenReturn(Optional.empty());
        //when
        Optional<QuestTo> questTo = questService.create(testQuestTo);
        //given
        assertFalse(questTo.isPresent());
    }

    @Test
    void givenTestQuestTo_whenDelete_thenVerifyDelete() {
        //when
        questService.delete(savedTestQuestTo);

        //then
        verify(questRepositoryMock).delete(savedTestQuest);
    }

    @Test
    void givenTestQuestId_whenDelete_thenVerifyDelete() {
        //given
        long questId = savedTestQuestTo.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));

        //when
        questService.delete(questId);

        //then
        verify(questRepositoryMock).delete(savedTestQuest);
    }

    @Test
    void givenQuests_whenGetAll_thenVerifyGetAll() {
        //given //when
        questService.getAll();
        //then
        verify(questRepositoryMock).getAll();
    }

    @Test
    void givenQuestTo_whenGetAll_thenVerifyGetAll() {
        //given
        Collection<Quest> quests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            quests.add(Quest.builder().id(5L + i).build());
        }
        when(questRepositoryMock.getAll()).thenReturn(quests);

        //when
        Collection<QuestTo> questTos = questService.getAll();

        //then
        assertEquals(quests.size(), questTos.size());
        assertEquals(quests.stream().map(Dto.MAPPER::from).toList(), questTos);
    }

    @Test
    void givenNotValidQuest__whenGetQuestWithQuestions_thenThrowException() {
        //given
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.empty());
        //when //then
        assertThrows(Exception.class, () -> questService.getQuestWithQuestions(1L));
    }

    @Test
    void givenssNotValidQuest__whenGetQuestWithQuestions_thenThrowException() {
        //given
        long questId = savedTestQuest.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.ofNullable(savedTestQuest));
        //when
        QuestTo actual = questService.getQuestWithQuestions(questId) ;
        // then
        assertEquals(savedTestQuestTo, actual);
    }

    @Test
    void givenValidQuest__whenGetQuestWithQuestions_thenVerifyGet() {
        //given
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.of(savedTestQuest));
        //when
        questService.getQuestWithQuestions(savedTestQuest.getId());
        // then
        verify(questRepositoryMock).get(any(Long.class));
    }


//    public Optional<QuestTo> get(long id) {
//        return questRepository.get(id).map(Dto.MAPPER::from);
//    }
//

    @Test
    void givenQuestId__whenGet_thenVerifyGet() {
        //when
        questService.get(savedTestQuest.getId());
        //then
        verify(questRepositoryMock).get(any(Long.class));
    }

    @Test
    void givenNotValidQuestId__whenGet_thenReturnEmpty() {
        //given
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.empty());
        //when
        Optional<QuestTo> optionalQuestTo = questService.get(savedTestQuest.getId());
        //then
        assertTrue(optionalQuestTo.isEmpty());
    }

    @Test
    void givenValidQuestId__whenGet_thenReturnQuestTo() {
        //given
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.ofNullable(savedTestQuest));
        //when
        Optional<QuestTo> optionalQuestTo = questService.get(savedTestQuest.getId());
        //then
        assertTrue(optionalQuestTo.isPresent());
        assertEquals(savedTestQuestTo, optionalQuestTo.get());
    }

    @Test
    void gevenNewData_whenUpdate_thenQuestUpdated() {
        //given
        long questId = savedTestQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedTestQuestion.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));
        when(questionRepositoryMock.get(firstQuestionId)).thenReturn(Optional.of(savedTestQuestion));

        //when
        questService.updateQuest(questId, name, description, firstQuestionId);

        //then
        assertEquals(savedTestQuest.getFirstQuestion(), savedTestQuestion);
        assertEquals(savedTestQuest.getName(), name);
        assertEquals(savedTestQuest.getDescription(), description);
    }

    @Test
    void gevenNewData_whenUpdate_thenVerifyGet() {
        //given
        long questId = savedTestQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedTestQuestion.getId();

        //when
        questService.updateQuest(questId, name, description, firstQuestionId);

        //then
        verify(questRepositoryMock).get(questId);
        verify(questionRepositoryMock).get(firstQuestionId);
    }

    @Test
    void gevenNotValidQuestId_whenUpdate_thenQuestNotUpdated() {
        //given
        long questId = savedTestQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedTestQuestion.getId();
        when(questRepositoryMock.get(any(Long.class))).thenReturn(Optional.empty());
        when(questionRepositoryMock.get(firstQuestionId)).thenReturn(Optional.of(savedTestQuestion));

        //when
        questService.updateQuest(questId, name, description, firstQuestionId);

        //then
        assertNotEquals(savedTestQuest.getFirstQuestion(), savedTestQuestion);
        assertNotEquals(savedTestQuest.getName(), name);
        assertNotEquals(savedTestQuest.getDescription(), description);
    }

    @Test
    void gevenNotValidQuestionId_whenUpdate_thenQuestNotUpdated() {
        //given
        long questId = savedTestQuest.getId();
        String name = "new name";
        String description = "new description";
        long firstQuestionId = savedTestQuestion.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));
        when(questionRepositoryMock.get(any(Long.class))).thenReturn(Optional.empty());

        //when
        questService.updateQuest(questId, name, description, firstQuestionId);

        //then
        assertNotEquals(savedTestQuest.getFirstQuestion(), savedTestQuestion);
        assertNotEquals(savedTestQuest.getName(), name);
        assertNotEquals(savedTestQuest.getDescription(), description);
    }

    @Test
    void whenAddNewQuestionToCreatedQuest_thenQuestContainsQuestion() {
        //given
        long questId = savedTestQuest.getId();
        String questionMessage = "new question";

        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));

        // when
        questService.addNewQuestionToCreatedQuest(questId, questionMessage);
        Optional<Question> actual = savedTestQuest.getQuestions().stream().filter(question -> question.getQuestionMessage().equals(questionMessage)).findFirst();

        // then
        assertTrue(actual.isPresent());
    }

    @Test
    void whenAddNewQuestionToCreatedQuest_thenQuestContainsOneMoreQuestion() {
        //given
        long questId = savedTestQuest.getId();
        String questionMessage = "new question";
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));
        int expected = savedTestQuest.getQuestions().size();

        // when
        questService.addNewQuestionToCreatedQuest(questId, questionMessage);

        // then
        int actual = savedTestQuest.getQuestions().size();
        assertEquals(1, actual - expected);
    }

    @Test
    void whenAddNewQuestionToCreatedQuest_thenVerifyGet() {
        //given
        long questId = savedTestQuest.getId();
        String questionMessage = "new question";
        int expected = savedTestQuest.getQuestions().size();

        // when
        questService.addNewQuestionToCreatedQuest(questId, questionMessage);

        // then
        verify(questRepositoryMock).get(questId);
    }

    @Test
    void whenAddNewQuestionToCreatedQuest_thenVerifyCreate() {
        //given
        long questId = savedTestQuest.getId();
        String questionMessage = "new question";
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));
        int expected = savedTestQuest.getQuestions().size();

        // when
        questService.addNewQuestionToCreatedQuest(questId, questionMessage);

        // then
        verify(questRepositoryMock).get(questId);
    }

    @Test
    void givenNotValidQuestId_whenAddNewQuestionToCreatedQuest_thenDoesNotThrowException() {
        //given
        long questId = savedTestQuest.getId();
        String questionMessage = "new question";
        when(questRepositoryMock.get(questId)).thenReturn(Optional.empty());
        int expected = savedTestQuest.getQuestions().size();

        // when // then
        assertDoesNotThrow(() -> questService.addNewQuestionToCreatedQuest(questId, questionMessage));
    }

    @Test
    void givenNotValidQuestId_whenAddNewQuestionToCreatedQuest_thenThrowException() {
        //given // when
        long questId = savedTestQuest.getId();
        String questionMessage = null;
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));

        // then
        assertThrows(Exception.class, () -> questService.addNewQuestionToCreatedQuest(questId, questionMessage));
    }
    @Test
    void givenNotValidQuestId_whenAddNewQuestionToCreatedQuest_thenThrowQuestException() {
        //given // when
        long questId = savedTestQuest.getId();
        String questionMessage = null;
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(savedTestQuest));

        // then
        assertThrows(QuestException.class, () -> questService.addNewQuestionToCreatedQuest(questId, questionMessage));
    }
}