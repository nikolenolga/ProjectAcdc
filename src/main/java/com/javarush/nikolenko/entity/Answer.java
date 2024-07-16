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
@Table(name = "answer", schema = "public")
public class Answer implements AbstractComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_message")
    protected String answerMessage;
    @Column(name = "final_message")
    private String finalMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state", nullable = false)
    protected GameState gameState;

    @ManyToOne
    @JoinColumn(name = "next_question_id")
    @ToString.Exclude
    private Question nextQuestion;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    private Question question;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return id != null && Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return 38;
    }

    public boolean hasNextQuestion() {
        return nextQuestion != null && nextQuestion.getId() != 0L;
    }

    public boolean hasFinalMessage() {
        return finalMessage != null && !finalMessage.isBlank();
    }

    public boolean isWin() {
        return gameState == GameState.WIN;
    }

    public boolean isFinal() {
        return gameState != GameState.GAME;
    }
}
