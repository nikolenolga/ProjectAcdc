package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
@ToString
public class Game extends AbstractComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameState gameState;
    private Long firstQuestionId;
    private Long currentQuestionId;

    private Long userPlayerId;
    private Long questId;

    public void restart() {
        this.gameState = GameState.GAME;
        this.currentQuestionId = this.firstQuestionId;
        log.debug("Game restarted, gameId - {}, questId - {}, userId - {}", id, questId, userPlayerId);
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
    }

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }
}
