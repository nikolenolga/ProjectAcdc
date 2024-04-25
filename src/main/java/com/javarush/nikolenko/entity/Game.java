package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Game extends AbstractComponent {
    private Long userPlayerId;
    private Long questId;
    private Long currentQuestionId;
    private Long firstQuestionId;
    private GameState gameState;

    public Game(long userPlayerId, long questId, long currentQuestionId) {
        super(0L);
        this.userPlayerId = userPlayerId;
        this.questId = questId;
        this.currentQuestionId = currentQuestionId;
        this.firstQuestionId = currentQuestionId;
        this.gameState = GameState.GAME;
    }

    public void restart() {
        this.gameState = GameState.GAME;
        this.currentQuestionId = this.firstQuestionId;
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
    }
}
