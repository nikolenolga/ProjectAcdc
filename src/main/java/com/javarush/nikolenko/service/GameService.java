package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.GameRepository;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.UserRepository;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
public class GameService {
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestRepository questRepository;
    private final GameRepository gameRepository;
    private final AnswerService answerService;
    private final QuestService questService;

//    public Optional<GameTo> create(GameTo gameTo) {
//        if (validateGame(gameTo)) {
//            return gameRepository.create(Dto.MAPPER.from(gameTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("Game creation failed, game - {}", gameTo);
//        return Optional.empty();
//    }
//
//    public Optional<GameTo> update(GameTo gameTo) {
//        if (validateGame(gameTo)) {
//            return gameRepository.update(Dto.MAPPER.from(gameTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("Game updating failed, game - {}", gameTo);
//        return Optional.empty();
//    }
//
//    public void delete(GameTo gameTo) {
//        gameRepository.delete(Dto.MAPPER.from(gameTo));
//    }
//
//    public Collection<GameTo> getAll() {
//        return gameRepository.getAll().stream().map(Dto.MAPPER::from).toList();
//    }
//
//    public Optional<GameTo> get(long id) {
//        return gameRepository.get(id).map(Dto.MAPPER::from);
//    }


    public GameTo initGame(long playerId, long questId) {
        Optional<Quest> optionalQuest = questRepository.get(questId);
        User player = userRepository.get(playerId).orElse(null);
        if (optionalQuest.isEmpty()) {
            throw new QuestException("Quest with %d not found".formatted(questId));
        }
        Quest quest = optionalQuest.get();
        Game game = Game.builder()
                .quest(quest)
                .player(player)
                .gameState(GameState.GAME)
                .currentQuestion(quest.getFirstQuestion())
                .build();

        return gameRepository.create(game)
                .map(Dto.MAPPER::from)
                .orElse(null);
    }

    public QuestionTo getCurrentQuestionWithAnswers(long id) {
        Question question = gameRepository.get(id).map(Game::getCurrentQuestion).orElseThrow();
        question.getPossibleAnswers();
        return Dto.MAPPER.from(question);
    }

    public boolean processAnswer(long answerId, long gameId) {
        Answer answer = answerRepository.get(answerId).get();
        Question nextQuestion = answer.getNextQuestion();
        Game game = gameRepository.get(gameId).orElseThrow();
        game.setCurrentQuestion(nextQuestion);

        return answer.hasFinalMessage() || answer.isFinal();
    }

//
//    public void setNextQuestion(long gameId, long nextQuestionId) {
//        Optional<Game> optionalGame = gameRepository.get(gameId);
//        optionalGame.ifPresent(game -> game.setCurrentQuestion(nextQuestion));
//    }

    public void restartGame(long gameId) {
        gameRepository.get(gameId).ifPresent(Game::restart);
        log.debug("Game {} restarted.", gameId);
    }

    public AnswerTo checkAnswer(long answerId, long gameId) {
        Answer answer = answerRepository.get(answerId).get();

        if (answer.isFinal()) {
            Game game = gameRepository.get(gameId).get();
            game.setGameState(answer.isWin() ? GameState.WIN : GameState.LOSE);
            log.debug("Game {} finished {}.", game.getId(), game.getGameState());
        }

        return Dto.MAPPER.from(answer);
    }

//    private boolean validateGame(GameTo gameTo) {
//        return gameTo != null && ObjectUtils.allNotNull(gameTo.getQuestId());
//    }
}
