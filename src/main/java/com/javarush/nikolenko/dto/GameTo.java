package com.javarush.nikolenko.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameTo {
    Long id;
    GameState gameState;
    Long currentQuestionId;
    Long playerId;
    Long questId;
}
