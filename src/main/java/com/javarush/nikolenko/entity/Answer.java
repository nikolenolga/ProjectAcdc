package com.javarush.nikolenko.entity;

public class Answer extends AbstractComponent {
    protected String answerMessage;
    protected GameState gameState;
    private long nextQuestionId;
    private String finalMessage;


    public Answer() {
    }

    public Answer(long id, String answerMessage, GameState gameState, long nextQuestionId, String finalMessage) {
        super(id);
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = finalMessage;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public GameState getGameState() {
        return gameState;
    }

    public long getNextQuestionId() {
        return nextQuestionId;
    }

    public String getFinalMessage() {
        return finalMessage;
    }

    public boolean isFinal() {
        return gameState != GameState.GAME;
    }

    public boolean hasNextQuestion() {
        return nextQuestionId != 0L;
    }

    public boolean hasFinalMessage() {
        return !finalMessage.isBlank();
    }

    public boolean isWin() {
        return gameState == GameState.WIN;
    }

    public boolean isLose() {
        return gameState == GameState.LOSE;
    }

    public boolean hasNextQuestionLogic() {
        return !hasFinalMessage() && !isFinal() && hasNextQuestion();
    }

}
