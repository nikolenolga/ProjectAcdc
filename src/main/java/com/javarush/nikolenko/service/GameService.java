package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.repository.GameRepository;

import java.util.Collection;
import java.util.Optional;

public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void create(Game game) {
        gameRepository.create(game);
    }

    public void update(Game game) {
        gameRepository.update(game);
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

    public long getFirstQuestionId(long id) {
        return get(id).map(Game::getFirstQuestionId).orElse(0L);
    }

    public long getCurrentQuestionId(long id) {
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
