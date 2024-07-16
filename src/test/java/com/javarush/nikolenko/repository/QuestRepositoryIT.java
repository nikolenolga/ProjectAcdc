package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.ContainerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuestRepositoryIT extends ContainerIT {
    private static final SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
    private static final QuestRepository questRepository = new QuestRepository(sessionCreater);
    private static final QuestionRepository questionRepository = new QuestionRepository(sessionCreater);

    private Quest testQuest;
    private Question testQuestion;

    @BeforeEach
    void setUp() {
        sessionCreater.beginTransactional();

        testQuest = Quest.builder()
                .name("Test Quest")
                .description("Test Description")
                .build();
        questRepository.create(testQuest);

        testQuestion = Question.builder()
                .quest(testQuest)
                .questionMessage("First Message")
                .build();
        questionRepository.create(testQuestion);

        testQuest.setFirstQuestion(testQuestion);
    }

    @Test
    void givenCreatedTestQuest_whenGetId_thenIdNotNullAndNot0L() {
        assertTrue(testQuest.getId() != null && testQuest.getId() != 0L);
        assertTrue(testQuest.getFirstQuestion().getId() != null && testQuest.getFirstQuestion().getId() != 0L);
    }

    @Test
    void givenCreatedTestQuest_whenGetQuestWithId_thenEqualQuest() {
        Optional<Quest> optionalQuest = questRepository.get(testQuest.getId());
        assertTrue(optionalQuest.isPresent());
        Quest quest = optionalQuest.get();
        assertEquals(testQuest, quest);
    }

    @Test
    void givenCreatedTestQuest_whenSetNewName_thenGetQuestWithUpdatedName() {
        testQuest.setName("Updated name");
        questRepository.update(testQuest);

        Optional<Quest> optionalQuest = questRepository.get(testQuest.getId());
        assertTrue(optionalQuest.isPresent());
        Quest quest = optionalQuest.get();
        assertEquals(testQuest, quest);
        assertEquals(testQuest.getName(), quest.getName());
    }

    @Test
    void givenCreatedTestQuest_whenDeleteQuest_thenGetEmptyOptionalQuest() {
        Quest testQuestForDelete = Quest.builder()
                .name("Test Quest")
                .description("Test Description")
                .build();
        questRepository.create(testQuestForDelete);

        questRepository.delete(testQuestForDelete);
        Optional<Quest> optionalQuest = questRepository.get(testQuestForDelete.getId());
        assertTrue(optionalQuest.isEmpty());
    }

    @Test
    void givenCreatedTestQuest_whenDeleteQuestById_thenGetEmptyOptionalQuest() {
        Quest testQuestForDelete = Quest.builder()
                .name("Test Quest")
                .description("Test Description")
                .build();
        questRepository.create(testQuestForDelete);

        questRepository.delete(testQuestForDelete.getId());
        Optional<Quest> optionalQuest = questRepository.get(testQuestForDelete.getId());
        assertTrue(optionalQuest.isEmpty());
    }

    @Test
    void givenCreatedQuests_whenGetAll_thenGetNotEmptyCollection() {
        for (int i = 16; i < 30; i++) {
            questRepository.create(Quest.builder()
                    .firstQuestion(testQuestion)
                    .name("Test Quest " + i)
                    .description("Test Description " + i)
                    .build()
            );
        }
        Collection<Quest> quests = questRepository.getAll();
        assertFalse(quests.isEmpty());
    }

    @Test
    void givenGetAllQuestsCount_whenDeleteOneQuest_thenGetAllQuestsDeltaIsOne() {
        int expected = questRepository.getAll().size();

        for (int i = 0; i < 15; i++) {
            questRepository.create(Quest.builder()
                    .firstQuestion(testQuestion)
                    .name("Test Quest " + i)
                    .description("Test Description " + i)
                    .build()
            );
        }

        int actual = questRepository.getAll().size();

        assertEquals(15, actual - expected);
    }

    @AfterEach
    void tearDown() {
        questRepository.delete(testQuest);
        sessionCreater.endTransactional();
    }
}