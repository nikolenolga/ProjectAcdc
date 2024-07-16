package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.GameRepository;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {
    private GameRepository gameRepositoryMock;
    private QuestRepository questRepositoryMock;
    private UserRepository userRepositoryMock;
    private AnswerRepository answerRepositoryMock;
    private AnswerService answerServiceMock;
    private QuestService questServiceMock;
    private GameService gameService;
    private Game game;
    private Game savedGame;
    private GameTo savedGameTo;
    private User user;
    private Answer gameAnswer;
    private Answer finalAnswer;
    private Answer finalAnswerWithoutFinalMessage;
    private Answer gameAnswerWithFinalMessage;
    private Quest quest;
    private Question question;
    private Question nextQuestion;
    private QuestionTo questionTo;

    @BeforeEach
    void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        answerRepositoryMock = mock(AnswerRepository.class);
        questRepositoryMock = mock(QuestRepository.class);
        gameRepositoryMock = mock(GameRepository.class);
        answerServiceMock = mock(AnswerService.class);
        questServiceMock = mock(QuestService.class);
        gameService = new GameService(userRepositoryMock, answerRepositoryMock, questRepositoryMock, gameRepositoryMock, answerServiceMock, questServiceMock);

        user = User.builder()
                .id(8L)
                .login("testLogin")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();

        quest = Quest.builder()
                .id(3L)
                .name("Test Quest")
                .description("Test Description")
                .build();

        question = Question.builder()
                .id(14L)
                .quest(quest)
                .questionMessage("First Message")
                .build();

        quest.setFirstQuestion(question);

        nextQuestion = Question.builder()
                .id(22L)
                .quest(quest)
                .questionMessage("Next Question Message")
                .build();

        gameAnswer = Answer.builder()
                .id(15L)
                .answerMessage("Answer message")
                .gameState(GameState.GAME)
                .nextQuestion(nextQuestion)
                .build();
        question.addPossibleAnswer(gameAnswer);

        gameAnswerWithFinalMessage = Answer.builder()
                .id(16L)
                .answerMessage("Answer message")
                .finalMessage("Final message")
                .gameState(GameState.GAME)
                .nextQuestion(nextQuestion)
                .build();
        question.addPossibleAnswer(gameAnswerWithFinalMessage);

        finalAnswer = Answer.builder()
                .id(17L)
                .answerMessage("Answer message")
                .finalMessage("Final message")
                .gameState(GameState.WIN)
                .build();
        question.addPossibleAnswer(finalAnswer);

        finalAnswerWithoutFinalMessage = Answer.builder()
                .id(18L)
                .answerMessage("Answer message")
                .gameState(GameState.LOSE)
                .build();
        question.addPossibleAnswer(finalAnswerWithoutFinalMessage);

        game = Game.builder()
                .gameState(GameState.GAME)
                .currentQuestion(question)
                .player(user)
                .quest(quest)
                .build();

        savedGame = Game.builder()
                .id(1L)
                .gameState(GameState.GAME)
                .currentQuestion(quest.getFirstQuestion())
                .player(user)
                .quest(quest)
                .build();

        savedGameTo = Dto.MAPPER.from(savedGame);
        questionTo = Dto.MAPPER.from(question);
    }

    @Test
    void givenUserIdAndQuestId_whenInitGame_thenVerifyCreateGame() {
        //given
        long playerId = user.getId();
        long questId = quest.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.of(quest));
        when(userRepositoryMock.get(playerId)).thenReturn(Optional.of(user));
        //when
        GameTo actualTo = gameService.initGame(playerId, questId);
        //then
        verify(questRepositoryMock).get(questId);
        verify(userRepositoryMock).get(playerId);
        verify(gameRepositoryMock).create(any(Game.class));
    }

    @Test
    void givenNotActualQuestId_whenInitGame_thenThrowException() {
        //given
        long playerId = user.getId();
        long questId = quest.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.empty());
        when(userRepositoryMock.get(playerId)).thenReturn(Optional.of(user));
        when(gameRepositoryMock.create(game)).thenReturn(Optional.of(savedGame));
        //when //then
        assertThrows(Exception.class, () -> gameService.initGame(playerId, questId));
    }

    @Test
    void givenNotActualQuestId_whenInitGame_thenThrowQuestException() {
        //given
        long playerId = user.getId();
        long questId = quest.getId();
        when(questRepositoryMock.get(questId)).thenReturn(Optional.empty());
        when(userRepositoryMock.get(playerId)).thenReturn(Optional.of(user));
        when(gameRepositoryMock.create(game)).thenReturn(Optional.of(savedGame));
        //when //then
        assertThrows(QuestException.class, () -> gameService.initGame(playerId, questId));
    }

    @Test
    void givenValidGameId_whenGetCurrentQuestionWithAnswers_thenGetQuestionToWithPossibleAnswersTo() {
        //given
        long gameId = savedGame.getId();
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.ofNullable(savedGame));
        //when //then
        assertEquals(questionTo, gameService.getCurrentQuestionWithAnswers(gameId));
    }

    @Test
    void givenNotValidGameId_whenGetCurrentQuestionWithAnswers_thenThrowException() {
        //given
        long gameId = 8L;
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.empty());
        //when //then
        assertThrows(Exception.class, () -> gameService.getCurrentQuestionWithAnswers(gameId));
    }

    @Test
    void givenFinalAnswerWithFinalMessage_whenProcessAnswer_thenReturnTrue() {
        //given
        long answerId = finalAnswer.getId();
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(finalAnswer));
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        boolean actual = gameService.processAnswer(answerId, gameId);

        // then
        assertTrue(actual);
    }

    @Test
    void givenNotFinalAnswerWithoutFinalMessage_whenProcessAnswer_thenReturnFalse() {
        //given
        long answerId = gameAnswer.getId();
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(gameAnswer));
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        boolean actual = gameService.processAnswer(answerId, gameId);

        // then
        assertFalse(actual);
    }

    @Test
    void givenNotFinalAnswerWithFinalMessage_whenProcessAnswer_thenReturnTrue() {
        //given
        long answerId = gameAnswerWithFinalMessage.getId();
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(gameAnswerWithFinalMessage));
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        boolean actual = gameService.processAnswer(answerId, gameId);

        // then
        assertTrue(actual);
    }

    @Test
    void givenFinalAnswerWithoutFinalMessage_whenProcessAnswer_thenReturnTrue() {
        //given
        long answerId = finalAnswerWithoutFinalMessage.getId();
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(finalAnswerWithoutFinalMessage));
        when(gameRepositoryMock.get(savedGame.getId())).thenReturn(Optional.of(savedGame));

        //when
        boolean actual = gameService.processAnswer(answerId, gameId);

        // then
        assertTrue(actual);
    }

    @Test
    void givenFinishedGame_whenRestartGame_ThenGameStateIsGame() {
        //given
        long gameId = savedGame.getId();
        savedGame.setGameState(GameState.WIN);
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        gameService.restartGame(gameId);

        // then
        assertEquals(GameState.GAME, savedGame.getGameState());
    }

    @Test
    void givenFinalLoseAnswer_whenCheckAnswer_thenGameStateIsLose() {
        //given
        long answerId = finalAnswerWithoutFinalMessage.getId();
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(finalAnswerWithoutFinalMessage));
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        gameService.checkAnswer(answerId, gameId);

        // then
        assertEquals(GameState.LOSE, savedGame.getGameState());
    }

    @Test
    void givenFinalWinAnswer_whenCheckAnswer_thenGameStateIsWin() {
        //given
        long answerId = finalAnswer.getId();
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(finalAnswer));
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        gameService.checkAnswer(answerId, gameId);

        // then
        assertEquals(GameState.WIN, savedGame.getGameState());
    }

    @Test
    void givenFinalAnswer_whenCheckAnswer_thenGameAndAnswerToHasEqualGameStates() {
        //given
        long answerId = 28L;
        Answer answer = Answer.builder().id(answerId).answerMessage("Answer message").gameState(GameState.values()[(int) (Math.random() * 3)]).build();
        if(!answer.isFinal()) answer.setNextQuestion(nextQuestion);
        question.addPossibleAnswer(answer);
        long gameId = savedGame.getId();
        when(answerRepositoryMock.get(answerId)).thenReturn(Optional.of(answer));
        when(gameRepositoryMock.get(gameId)).thenReturn(Optional.of(savedGame));

        //when
        AnswerTo answerTo = gameService.checkAnswer(answerId, gameId);

        // then
        assertEquals(answerTo.getGameState(), savedGame.getGameState());
    }

}