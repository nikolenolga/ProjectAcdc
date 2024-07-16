package com.javarush.nikolenko.entity;

import com.javarush.nikolenko.dto.GameState;
import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "game", schema = "public")
public class Game implements AbstractComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state", nullable = false)
    private GameState gameState;

    @ManyToOne
    @JoinColumn(name = "current_question_id")
    @ToString.Exclude
    private Question currentQuestion;

    @ManyToOne
    @JoinColumn(name = "player_id")
    @ToString.Exclude
    private User player;

    @ManyToOne
    @JoinColumn(name = "quest_id")
    @ToString.Exclude
    private Quest quest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id != null && Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return 18;
    }

    public void restart() {
        this.gameState = GameState.GAME;
        this.currentQuestion = this.quest.getFirstQuestion();
    }

    public boolean isFinished() {
        return this.gameState != GameState.GAME;
    }
}
