package com.javarush.nikolenko.entity;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends AbstractComponent {
    private Long id;
    protected String answerMessage;
    protected GameState gameState;
    private Long nextQuestionId;
    private String finalMessage;
    private Long questionId;

    public Answer(String answerMessage, GameState gameState, long nextQuestionId, String finalMessage) {
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = finalMessage;
    }

    public Answer(String answerMessage, GameState gameState, long nextQuestionId) {
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = "";
    }

    public Answer(String answerMessage, GameState gameState, long nextQuestionId, String finalMessage, Long questionId) {
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = finalMessage;
        this.questionId = questionId;
    }

    public Answer(String answerMessage, GameState gameState, long nextQuestionId, Long questionId) {
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = "";
        this.questionId = questionId;
    }

    public boolean isFinal() {
        return gameState != GameState.GAME;
    }

    public boolean hasNextQuestion() {
        return nextQuestionId != null && nextQuestionId != 0L;
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

    public boolean hasOnlyNextQuestionLogic() {
        return !hasFinalMessage() && !isFinal() && hasNextQuestion();
    }

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }

}
