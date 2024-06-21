package com.javarush.nikolenko.dto;

import com.javarush.nikolenko.entity.GameState;
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
}