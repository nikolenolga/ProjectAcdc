package com.javarush.nikolenko.dto;

import com.javarush.nikolenko.entity.GameState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameTo {
    Long id;
    GameState gameState;
    Long firstQuestionId;
    Long currentQuestionId;
    Long playerId;
    Long questId;
}
