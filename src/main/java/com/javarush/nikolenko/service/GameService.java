package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.repository.GameRepository;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Optional<Game> create(Game game) {
        if (ObjectUtils.allNotNull(game.getUserPlayerId(), game.getQuestId(), game.getCurrentQuestionId(), game.getFirstQuestionId())) {
            gameRepository.create(game);
            return Optional.of(game);
        }
        return Optional.empty();
    }

    public Optional<Game> update(Game game) {
        if (ObjectUtils.allNotNull(game.getUserPlayerId(), game.getQuestId(), game.getCurrentQuestionId(), game.getFirstQuestionId())) {
            gameRepository.update(game);
            return Optional.of(game);
        }
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
        Game game = new Game(userPlayerId, questId, currentQuestion);
        gameRepository.create(game);
        return game;
    }

    public Long getFirstQuestionId(long id) {
        return get(id).map(Game::getFirstQuestionId).orElse(0L);
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
}
