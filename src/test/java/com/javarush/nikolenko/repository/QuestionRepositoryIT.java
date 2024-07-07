package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.ContainerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import org.junit.jupiter.api.*;

import java.util.*;

class QuestionRepositoryIT extends ContainerIT {
    private static final SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
    private static final QuestionRepository questionRepository = new QuestionRepository(sessionCreater);
    private static final AnswerRepository answerRepository = new AnswerRepository(sessionCreater);
    private Answer testAnswer;
    private Question testQuestion;

    @BeforeEach
    void setUp() {
        QuestRepository questRepository = new QuestRepository(sessionCreater);
        sessionCreater.beginTransactional();

        testQuestion = Question.builder()
                .questionMessage("First Message")
                .build();
        questionRepository.create(testQuestion);
    }

    @Test
    void givenCreatedTestQuestion_whenGetId_thenIdNotNullAndNot0L() {
        Assertions.assertTrue(testQuestion.getId() != null && testQuestion.getId() != 0L);
    }

    @Test
    void givenCreatedTestQuestion_whenGetQuestionWithId_thenEqualQuestion() {
        Optional<Question> optionalQuestion = questionRepository.get(testQuestion.getId());
        Assertions.assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();
        Assertions.assertEquals(testQuestion, question);
    }

    @Test
    void givenCreatedTestQuestion_whenSetNewMessage_thenGetQuestionWithUpdatedMessage() {
        testQuestion.setQuestionMessage("First Message");
        questionRepository.update(testQuestion);

        Optional<Question> optionalQuestion = questionRepository.get(testQuestion.getId());
        Assertions.assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();
        Assertions.assertEquals(testQuestion, question);
        Assertions.assertEquals(testQuestion.getQuestionMessage(), question.getQuestionMessage());
    }


    @Test
    void givenCreatedTestQuestion_whenFindQuestionWithThisMessage_thenGetEqualQuestion() {
        Question searchedQuestion = Question.builder()
                .questionMessage("First Message")
                .build();
        Question actualQuestion = questionRepository.find(searchedQuestion).findFirst().orElse(null);
        Assertions.assertEquals(testQuestion, actualQuestion);
    }

    @Test
    void givenCreatedTestQuestion_whenDeleteQuestion_thenGetEmptyOptionalQuestion() {
        Question testQuestionForDelete = Question.builder()
                .questionMessage("First Message")
                .build();
        questionRepository.create(testQuestionForDelete);

        questionRepository.delete(testQuestionForDelete);
        Optional<Question> optionalQuestion = questionRepository.get(testQuestionForDelete.getId());
        Assertions.assertTrue(optionalQuestion.isEmpty());
    }

    @Test
    void givenCreatedTestQuestion_whenDeleteQuestionById_thenGetEmptyOptionalQuestion() {
        Question testQuestionForDelete = Question.builder()
                .questionMessage("First Message")
                .build();
        questionRepository.create(testQuestionForDelete);

        questionRepository.delete(testQuestionForDelete.getId());
        Optional<Question> optionalQuestion = questionRepository.get(testQuestionForDelete.getId());
        Assertions.assertTrue(optionalQuestion.isEmpty());
    }

    @Test
    void givenCreatedQuestions_whenGetAll_thenGetNotEmptyCollection() {
        for (int i = 16; i < 30; i++) {
            questionRepository.create(Question.builder()
                    .questionMessage("First Message " + i)
                    .build()
            );
        }
        Collection<Question> questions = questionRepository.getAll();
        Assertions.assertFalse(questions.isEmpty());
    }
    @Test
    void givenGetAllQuestionsCount_whenCreateFiveQuestions_thenGetAllQuestionsDeltaIsFive() {
        Collection<Question> questions = questionRepository.getAll();
        int before = questions.size();

        for  (int i = 0; i < 5; i++) {
            questionRepository.create(Question.builder()
                    .questionMessage("First Message " + i)
                    .build()
            );
        }
        int actual = questionRepository.getAll().size() - before;

        Assertions.assertEquals(5, actual);
    }

    @Test
    void givenCreatedQuestionWithAnswers_whenGetAnswersByQuestionId_thenGetAllAnswersWithQuestionId() {
        //given
        Collection<Answer> expectedAnswers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GameState[] gameStates = GameState.values();
            int gameStatesIndex = (int) (Math.random() * gameStates.length);
            Answer answer = Answer.builder()
                    .answerMessage("Answer message" + i)
                    .gameState(gameStates[gameStatesIndex])
                    .build();
            answerRepository.create(answer);
            testQuestion.addPossibleAnswer(answer);
            expectedAnswers.add(answer);
        }

        //when
        Collection<Answer> actualAnswers = questionRepository.getAnswersByQuestionId(testQuestion.getId());

        //then
        Assertions.assertFalse(actualAnswers.isEmpty());
        Assertions.assertFalse(expectedAnswers.isEmpty());
        Assertions.assertEquals(expectedAnswers.size(), actualAnswers.size());
        Assertions.assertEquals(expectedAnswers, actualAnswers);
    }

    @AfterEach
    void tearDown() {
        questionRepository.delete(testQuestion);
        sessionCreater.endTransactional();
    }

    @AfterAll
    static void checkTestUsersDeleted() {
        Question question = Question.builder()
                .questionMessage("First Message")
                .build();

        List<Question> questions = questionRepository.find(question).toList();
        Assertions.assertTrue(questions.isEmpty());
    }
}