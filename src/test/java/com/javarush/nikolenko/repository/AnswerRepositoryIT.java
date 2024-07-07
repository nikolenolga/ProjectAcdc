package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.ContainerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.entity.Answer;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class AnswerRepositoryIT extends ContainerIT {
    private static final SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
    private static final AnswerRepository answerRepository = new AnswerRepository(sessionCreater);
    private Answer testAnswer;

    @BeforeEach
    void setUp() {
        sessionCreater.beginTransactional();

        GameState[] gameStates = GameState.values();
        int gameStatesIndex = (int) (Math.random() * gameStates.length);
        testAnswer = Answer.builder()
                .answerMessage("Answer message")
                .gameState(gameStates[gameStatesIndex])
                .build();
        answerRepository.create(testAnswer);
    }

    @Test
    void givenCreatedTestAnswer_whenGetId_thenIdNotNullAndNot0L() {
        Assertions.assertTrue(testAnswer.getId() != null && testAnswer.getId() != 0L);
    }

    @Test
    void givenCreatedTestAnswerr_whenGetAnswerWithId_thenEqualUser() {
        Optional<Answer> optionalAnswer = answerRepository.get(testAnswer.getId());
        Assertions.assertTrue(optionalAnswer.isPresent());
        Answer answer = optionalAnswer.get();
        Assertions.assertEquals(testAnswer, answer);
    }

    @Test
    void givenCreatedTestAnswer_whenSetNewLogin_thenGetAnswerWithUpdatedLogin() {
        testAnswer.setFinalMessage("New final Message");
        answerRepository.update(testAnswer);
        Optional<Answer> optionalAnswer = answerRepository.get(testAnswer.getId());
        Assertions.assertTrue(optionalAnswer.isPresent());
        Answer answer = optionalAnswer.get();
        Assertions.assertEquals(testAnswer, answer);
    }

    @Test
    void givenCreatedTestAnswer_whenFindAnswerWithThisNameAndLogin_thenGetEqualAnswer() {
        Answer answer = Answer.builder()
                .answerMessage(testAnswer.getAnswerMessage())
                .build();
        Stream<Answer> answerStream = answerRepository.find(answer);
        Assertions.assertEquals(testAnswer, answerStream.findFirst().orElseThrow());
    }

    @Test
    void givenCreatedTestAnswer_whenDeleteAnswer_thenGetEmptyOptionalAnswer() {
        Answer testAnswerForDelete = Answer.builder()
                .answerMessage("Answer message")
                .gameState(GameState.GAME)
                .build();
        answerRepository.create(testAnswerForDelete);

        answerRepository.delete(testAnswerForDelete);
        Optional<Answer> optionalAnswer = answerRepository.get(testAnswerForDelete.getId());
        Assertions.assertTrue(optionalAnswer.isEmpty());
    }

    @Test
    void givenCreatedTestAnswer_whenDeleteAnswerById_thenGetEmptyOptionalAnswer() {
        Answer testAnswerForDelete = Answer.builder()
                .answerMessage("Answer message")
                .gameState(GameState.GAME)
                .build();
        answerRepository.create(testAnswerForDelete);

        answerRepository.delete(testAnswerForDelete.getId());
        Optional<Answer> optionalAnswer = answerRepository.get(testAnswerForDelete.getId());
        Assertions.assertTrue(optionalAnswer.isEmpty());
    }

    @Test
    void givenCreatedUsers_whenGetAll_thenGetNotEmptyCollection() {
        for (int i = 16; i < 30; i++) {
            GameState[] gameStates = GameState.values();
            int gameStatesIndex = (int) (Math.random() * gameStates.length);

            answerRepository.create(Answer.builder()
                    .answerMessage("Answer message " + i)
                    .gameState(gameStates[gameStatesIndex])
                    .build());
        }
        Collection<Answer> answers = answerRepository.getAll();
        Assertions.assertFalse(answers.isEmpty());
    }

    @Test
    void givenGetAllUsersCount_whenDeleteOneUser_thenGetAllUsersDeltaIsOne() {
        for (int i = 0; i < 15; i++) {
            GameState[] gameStates = GameState.values();
            int gameStatesIndex = (int) (Math.random() * gameStates.length);

            answerRepository.create(Answer.builder()
                    .answerMessage("Answer message " + i)
                    .gameState(gameStates[gameStatesIndex])
                    .build());
        }
        Collection<Answer> answers = answerRepository.getAll();
        int expected = answers.size();

        Answer answer = answers.stream().toList().get((int) (Math.random() * answers.size()));
        answerRepository.delete(answer);

        Collection<Answer> answersAfterDelete = answerRepository.getAll();
        int actual = answersAfterDelete.size();

        Assertions.assertEquals(expected - actual, 1);
    }

    @AfterEach
    void tearDown() {
        answerRepository.delete(testAnswer);
        sessionCreater.endTransactional();
    }

    @AfterAll
    static void checkTestUsersDeleted() {
        Answer answer = Answer.builder().answerMessage("Answer message").build();

        List<Answer> answers = answerRepository.find(answer).toList();
        Assertions.assertTrue(answers.isEmpty());
    }

}