package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.repository.GameRepository;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final AnswerService answerService;

    @SneakyThrows
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        answerService = NanoSpring.find(AnswerService.class);
        log.info("GameService created");
    }

    public Optional<Game> create(Game game) {
        if (game != null && ObjectUtils.allNotNull(game.getUserPlayerId(), game.getQuestId(), game.getCurrentQuestionId(), game.getFirstQuestionId())) {
            gameRepository.create(game);
            return Optional.of(game);
        }
        log.debug("Game creation failed, game - {}", game);
        return Optional.empty();
    }

    public Optional<Game> update(Game game) {
        if (game != null && ObjectUtils.allNotNull(game.getUserPlayerId(), game.getQuestId(), game.getCurrentQuestionId(), game.getFirstQuestionId())) {
            gameRepository.update(game);
            return Optional.of(game);
        }
        log.debug("Game updating failed, game - {}", game);
        return Optional.empty();
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

    public Collection<Game> getAll() {
        return gameRepository.getAll();
    }

    public Optional<Game> get(long id) {
        return gameRepository.get(id);
    }

    public Game initGame(long userPlayerId, long questId, long currentQuestion) {
        Game game = new Game(0L, GameState.GAME, currentQuestion, currentQuestion, userPlayerId, questId);
        gameRepository.create(game);
        return game;
    }

    public Long getCurrentQuestionId(long id) {
        return get(id).map(Game::getCurrentQuestionId).orElse(0L);
    }

    public void setNextQuestion(long gameId, long nextQuestionId) {
        Optional<Game> optionalGame = gameRepository.get(gameId);
        optionalGame.ifPresent(game -> game.setCurrentQuestionId(nextQuestionId));
    }

    public void restartGame(long gameId) {
        Optional<Game> optionalGame = gameRepository.get(gameId);
        optionalGame.ifPresent(Game::restart);
    }

    public void checkAnswer(long answerId, HttpServletRequest req) {
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        if (optionalAnswer.isPresent() && optionalAnswer.get().isFinal()) {
            Answer answer = optionalAnswer.get();
            HttpSession session = req.getSession();
            long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);
            Game game = get(gameId).get();
            game.setGameState(answer.isWin() ? GameState.WIN : GameState.LOSE);
            update(game);
            log.info("Game {} finished {} and saved to repository.", game.getId(), game.getGameState());
        }
    }
}
