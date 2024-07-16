package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.ContainerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.User;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryIT extends ContainerIT {
    private static final SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
    private static final GameRepository gameRepository = new GameRepository(sessionCreater);
    private static final UserRepository userRepository = new UserRepository(sessionCreater);

    private Game testGame;
    private User testUser;

    @BeforeEach
    void setUp() {
        sessionCreater.beginTransactional();

        testUser = User.builder()
                .login("testLogin")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();
        userRepository.create(testUser);

        GameState[] gameStates = GameState.values();
        int gameStatesIndex = (int) (Math.random() * gameStates.length);
        testGame = Game.builder()
                .player(testUser)
                .gameState(gameStates[gameStatesIndex])
                .build();
        gameRepository.create(testGame);
    }

    @Test
    void givenCreatedTestGame_whenGetId_thenIdNotNullAndNot0L() {
        assertTrue(testGame.getId() != null && testGame.getId() != 0L);
    }

    @Test
    void givenCreatedTestGame_whenGetGameWithId_thenEqualGame() {
        Optional<Game> optionalGame = gameRepository.get(testGame.getId());
        assertTrue(optionalGame.isPresent());
        Game game = optionalGame.get();
        assertEquals(testGame, game);
    }

    @Test
    void givenCreatedTestGame_whenSetNewGameState_thenGetGameWithUpdatedLogin() {
        testGame.setGameState(testGame.isFinished() ? GameState.GAME : GameState.LOSE);
        gameRepository.update(testGame);
        Optional<Game> optionalGame = gameRepository.get(testGame.getId());
        assertTrue(optionalGame.isPresent());
        Game game = optionalGame.get();
        assertEquals(testGame, game);
    }

    @Test
    void givenCreatedTestGame_whenDeleteGame_thenGetEmptyOptionalGame() {
        Game testGameForDelete = Game.builder()
                .player(testUser)
                .gameState(GameState.GAME)
                .build();
        gameRepository.create(testGameForDelete);

        gameRepository.delete(testGameForDelete);
        Optional<Game> optionalGame = gameRepository.get(testGameForDelete.getId());
        assertTrue(optionalGame.isEmpty());
    }

    @Test
    void givenCreatedTestGame_whenDeleteGameById_thenGetEmptyOptionalGame() {
        gameRepository.delete(testGame.getId());
        Optional<Game> optionalGame = gameRepository.get(testGame.getId());
        assertTrue(optionalGame.isEmpty());
    }

    @Test
    void givenCreatedGames_whenGetAll_thenGetNotEmptyCollection() {
        for (int i = 16; i < 30; i++) {
            GameState[] gameStates = GameState.values();
            int gameStatesIndex = (int) (Math.random() * gameStates.length);

            gameRepository.create(Game.builder()
                    .player(testUser)
                    .gameState(gameStates[gameStatesIndex])
                    .build());
        }
        Collection<Game> games = gameRepository.getAll();
        assertFalse(games.isEmpty());
    }

    @Test
    void givenGetAllGamesCount_whenDeleteOneGame_thenGetAllGamesDeltaIsOne() {
        for (int i = 0; i < 15; i++) {
            GameState[] gameStates = GameState.values();
            int gameStatesIndex = (int) (Math.random() * gameStates.length);

            gameRepository.create(Game.builder()
                    .player(testUser)
                    .gameState(gameStates[gameStatesIndex])
                    .build());
        }
        Collection<Game> games = gameRepository.getAll();
        int expected = games.size();

        Game answer = games.stream().toList().get((int) (Math.random() * games.size()));
        gameRepository.delete(answer);

        Collection<Game> gamesAfterDelete = gameRepository.getAll();
        int actual = gamesAfterDelete.size();

        assertEquals(expected - actual, 1);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
        gameRepository.delete(testGame);
        sessionCreater.endTransactional();
    }
}