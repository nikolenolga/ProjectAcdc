package com.javarush.nikolenko.entity;

public class Game extends AbstractComponent {
    private long currentQuestion;
    private GameState gameState;
    private long questId;

    public Game(long id, long currentQuestion, GameState gameState, long questId) {
        super(id);
        this.currentQuestion = currentQuestion;
        this.gameState = gameState;
        this.questId = questId;
    }
}
