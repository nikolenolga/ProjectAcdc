package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.repository.GameRepository;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final AnswerService answerService;
    private final QuestService questService;

    public Optional<Game> create(Game game) {
        if (game != null && ObjectUtils.allNotNull(game.getPlayer().getId(), game.getQuest().getId(), game.getCurrentQuestionId(), game.getFirstQuestionId())) {
            gameRepository.create(game);
            return Optional.of(game);
        }
        log.debug("Game creation failed, game - {}", game);
        return Optional.empty();
    }

    public Optional<Game> update(Game game) {
        if (game != null && ObjectUtils.allNotNull(game.getPlayer().getId(), game.getQuest().getId(), game.getCurrentQuestionId(), game.getFirstQuestionId())) {
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

    public Game initGame(User player, long questId) {
        Optional<Quest> optionalQuest = questService.get(questId);
        if (optionalQuest.isEmpty()) {
            throw new QuestException("Quest with %d not found".formatted(questId));
        }
        Quest quest = optionalQuest.get();
        Game game = Game.builder()
                .quest(quest)
                .player(player)
                .gameState(GameState.GAME)
                .currentQuestionId(quest.getFirstQuestionId())
                .firstQuestionId(quest.getFirstQuestionId())
                .build();
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
