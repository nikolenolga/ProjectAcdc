package com.javarush.nikolenko.service;

import com.javarush.nikolenko.BaseControllerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class QuestParseIT extends BaseControllerIT {
    private static final SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);

    private static final String TEST_QUEST_TEXT = """
                : name : Default quest - Космопорт.
                : description : Пролог
                ? : 1 : Ты потерял память. Принять вызов НЛО?
                = : 2 : Принять вызов :
                < : 0 : Отклонить вызов : Ты отклонил вызов.
                ? : 2 : Ты принял вызов. Поднимаешься на мостик к капитану?
                = : 3 : Подняться на мостик :
                < : 0 : Отказаться подниматься на мостик : Ты не пошел на переговоры.
                ? : 3 : Ты поднялся на мостик. Ты кто?
                > : 0 : Рассказать правду о себе : Тебя вернули домой.
                < : 0 : Солгать о себе : Твою ложь разоблачили.""";
    private static final Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    QuestParseIT.class.getResource("/")
            ).toString()));
    private static final String validPath = CLASSES_ROOT + File.separator + "quests" + File.separator + "quest-test.txt";
    private long userId;
    private User user;

    private QuestEditService questEditService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        questEditService = NanoSpring.find(QuestEditService.class);
        userRepository = new UserRepository(sessionCreater);
        sessionCreater.beginTransactional();

        user = User.builder().role(Role.ADMIN).login("testLogin").password("testPassword").name("TestName").build();
        userRepository.create(user);
        userId = user.getId();
    }


    @Test
    void givenQuestTextFullWithQuestData_whenParseQuest_thenReturnQuestToWithSixAnswers() {
        //given //when
        Optional<QuestTo> questTo = questEditService.parseQuest(userId, TEST_QUEST_TEXT);

        //then
        AtomicInteger countAnswers = new AtomicInteger();
        questTo.get().getQuestions().forEach(questionTo -> countAnswers.addAndGet(questionTo.getPossibleAnswers().size()));
        assertEquals(6, countAnswers.get());
    }

    @Test
    void givenQuestTextFullWithQuestData_whenParseQuest_thenReturnQuestToContainsCertainQuestion() {
        //given //when
        Optional<QuestTo> questTo = questEditService.parseQuest(userId, TEST_QUEST_TEXT);

        //then
        Optional<QuestionTo> optionalQuestionTo =  questTo.get()
                .getQuestions()
                .stream()
                .filter(questionTo -> questionTo.getQuestionMessage()
                        .equals("Ты поднялся на мостик. Ты кто?"))
                .findFirst();

        assertTrue(optionalQuestionTo.isPresent());
        assertEquals(2, optionalQuestionTo.get().getPossibleAnswers().size());
        assertEquals(2, optionalQuestionTo.get().getPossibleAnswers().stream().filter(AnswerTo::isFinal).toList().size());
        assertEquals(2, optionalQuestionTo.get().getPossibleAnswers().stream().filter(answerTo -> answerTo.getNextQuestionId() == null).toList().size());
    }

    @Test
    void givenQuestTextFullWithQuestData_whenParseQuest_thenReturnQuestToWithThreeQuestions() {
        //given //when
        Optional<QuestTo> questTo = questEditService.parseQuest(userId, TEST_QUEST_TEXT);

        //then
        int actual = questTo.get().getQuestions().size();
        assertEquals(3, actual);
    }

    @Test
    void givenQuestTextWithQuestData_whenParseQuest_thenReturnNotEmptyOptionalQuestTo() {
        //given //when
        Optional<QuestTo> questTo = questEditService.parseQuest(userId, TEST_QUEST_TEXT);

        //then
        assertTrue(questTo.isPresent());
    }

    @Test
    void givenNotValidUser_whenParseQuest_thenThrowException() {
        //given
        long noValidUserId = 0L;

        //when //then
        assertThrows(Exception.class, () -> questEditService.parseQuest(noValidUserId, TEST_QUEST_TEXT));
    }

    @Test
    void givenQuestTextWithQuestData_whenParseQuest_thenReturnQuestToWithValidName() {
        //given //when
        Optional<QuestTo> questTo = questEditService.parseQuest(userId, TEST_QUEST_TEXT);

        //then
        assertEquals("Default quest - Космопорт.", questTo.get().getName());
    }

    @Test
    void givenQuestTextWithQuestData_whenParseQuest_thenReturnQuestToWithValidDescription() {
        //given//when
        Optional<QuestTo> questTo = questEditService.parseQuest(userId, TEST_QUEST_TEXT);

        //then
        assertEquals("Пролог", questTo.get().getDescription());
    }

    @Test
    void givenValidPath_whenLoadTextFromFile_thenReturnTextEqualToTestQuestText() {
        //given // when
        String actual = questEditService.loadTextFromFile(validPath);
        //then
        assertEquals(TEST_QUEST_TEXT, actual);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(user);
        sessionCreater.endTransactional();
    }
}
