package com.javarush.nikolenko.entity;

import com.javarush.nikolenko.dto.GameState;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
@ToString
public class Game implements AbstractComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state")
    private GameState gameState;

    @ManyToOne
    @JoinColumn(name = "current_question_id")
    @ToString.Exclude
    private Question currentQuestion;

    @ManyToOne
    @JoinColumn(name = "user_player_id")
    @ToString.Exclude
    private User player;

    @ManyToOne
    @JoinColumn(name = "quest_id")
    @ToString.Exclude
    private Quest quest;

    public void restart() {
        this.gameState = GameState.GAME;
        this.currentQuestion = this.quest.getFirstQuestion();
        log.debug("Game restarted, gameId - {}, questId - {}, userId - {}", id, quest.getId(), player.getId());
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
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
