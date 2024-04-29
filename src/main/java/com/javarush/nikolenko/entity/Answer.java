package com.javarush.nikolenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
public class Answer extends AbstractComponent {
    protected String answerMessage;
    protected GameState gameState;
    private Long nextQuestionId;
    private String finalMessage;

    public Answer(String answerMessage, GameState gameState, long nextQuestionId, String finalMessage) {
        super(0L);
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = finalMessage;
    }

    public Answer(String answerMessage, GameState gameState, long nextQuestionId) {
        super(0L);
        this.answerMessage = answerMessage;
        this.gameState = gameState;
        this.nextQuestionId = nextQuestionId;
        this.finalMessage = "";
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

}
