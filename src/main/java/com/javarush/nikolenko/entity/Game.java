package com.javarush.nikolenko.entity;

public class Game extends AbstractComponent {
    private long userPlayerId;
    private long questId;
    private long currentQuestionId;
    private long firstQuestionId;
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
        this.currentQuestionId = this.firstQuestionId;
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
    }

    public long getFirstQuestionId() {
        return firstQuestionId;
    }

    public void setFirstQuestionId(long firstQuestionId) {
        this.firstQuestionId = firstQuestionId;
    }

    public long getUserPlayerId() {
        return userPlayerId;
    }

    public void setUserPlayerId(long userPlayerId) {
        this.userPlayerId = userPlayerId;
    }

    public long getCurrentQuestionId() {
        return currentQuestionId;
    }

    public void setCurrentQuestionId(long currentQuestionId) {
        this.currentQuestionId = currentQuestionId;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public long getQuestId() {
        return questId;
    }

    public void setQuestId(long questId) {
        this.questId = questId;
    }
}
