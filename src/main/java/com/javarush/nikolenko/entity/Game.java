package com.javarush.nikolenko.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game extends AbstractComponent {
    private Long id;
    private Long userPlayerId;
    private Long questId;
    private Long currentQuestionId;
    private Long firstQuestionId;
    private GameState gameState;

    public Game(long userPlayerId, long questId, long currentQuestionId) {
        this.userPlayerId = userPlayerId;
        this.questId = questId;
        this.currentQuestionId = currentQuestionId;
        this.firstQuestionId = currentQuestionId;
        this.gameState = GameState.GAME;
        log.debug("New Game entity created, gameId - {}, questId - {}, userId - {}", id, questId, userPlayerId);
    }

    public void restart() {
        this.gameState = GameState.GAME;
        this.currentQuestionId = this.firstQuestionId;
        log.debug("Game restarted, gameId - {}, questId - {}, userId - {}", id, questId, userPlayerId);
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
    }

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }
}
