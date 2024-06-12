package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;


import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answer")
@ToString
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Answer implements AbstractComponent, Serializable {
    @Serial
    private static final long serialVersionUID = -1798070786934154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_message")
    protected String answerMessage;
    @Column(name = "final_message")
    private String finalMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state")
    protected GameState gameState;
    @Column(name = "next_question_id")
    private Long nextQuestionId;

    @ManyToOne
    @Column(name = "question_id")
    @ToString.Exclude
    private Question question;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return 38;
    }
}
