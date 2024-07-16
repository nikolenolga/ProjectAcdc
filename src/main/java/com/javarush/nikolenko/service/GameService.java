package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.GameRepository;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.UserRepository;

import com.javarush.nikolenko.utils.LoggerConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public GameTo initGame(long playerId, long questId) {
        Optional<Quest> optionalQuest = questRepository.get(questId);
        User player = userRepository.get(playerId).orElse(null);
        if (optionalQuest.isEmpty()) {
            log.error(LoggerConstants.QUEST_WITH_NOT_FOUND, questId);
            throw new QuestException("Quest with %d not found".formatted(questId));
        }
        Quest quest = optionalQuest.get();
        Game game = Game.builder()
                .quest(quest)
                .player(player)
                .gameState(GameState.GAME)
                .currentQuestion(quest.getFirstQuestion())
                .build();
        gameRepository.create(game);

        return Dto.MAPPER.from(game);
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

    public void restartGame(long gameId) {
        gameRepository.get(gameId).ifPresent(Game::restart);
        log.debug(LoggerConstants.GAME_RESTARTED, gameId);
    }

    public AnswerTo checkAnswer(long answerId, long gameId) {
        Answer answer = answerRepository.get(answerId).get();

        if (answer.isFinal()) {
            Game game = gameRepository.get(gameId).get();
            game.setGameState(answer.isWin() ? GameState.WIN : GameState.LOSE);
            log.debug(LoggerConstants.GAME_FINISHED, game.getId(), game.getGameState());
        }

        return Dto.MAPPER.from(answer);
    }
}
