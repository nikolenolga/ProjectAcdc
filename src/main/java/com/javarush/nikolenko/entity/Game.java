package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

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
    @Column(name = "game_state")
    private GameState gameState;
    @Column(name = "first_question_id")
    private Long firstQuestionId;
    @Column(name = "current_question_id")
    private Long currentQuestionId;

    @ManyToOne
    @Column(name = "user_player_id")
    @ToString.Exclude
    private User player;

    @ManyToOne
    @Column(name = "quest_id")
    @ToString.Exclude
    private Quest quest;

    public void restart() {
        this.gameState = GameState.GAME;
        this.currentQuestionId = this.firstQuestionId;
        log.debug("Game restarted, gameId - {}, questId - {}, userId - {}", id, quest.getId(), player.getId());
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
    }

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return 18;
    }
}
