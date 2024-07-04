package com.javarush.nikolenko.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerTo {
    Long id;
    String answerMessage;
    String finalMessage;
    GameState gameState;
    Long nextQuestionId;
    Long questionId;
    String image;

    public boolean isFinal() {
        return gameState != GameState.GAME;
    }

    public boolean isWin() {
        return gameState == GameState.WIN;
    }

    public boolean isLose() {
        return gameState == GameState.LOSE;
    }

}