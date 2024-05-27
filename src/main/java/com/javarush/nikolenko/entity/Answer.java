package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answer")
@ToString
public class Answer extends AbstractComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String answerMessage;
    private String finalMessage;

    @Enumerated(EnumType.STRING)
    protected GameState gameState;

    private Long nextQuestionId;
    private Long questionId;

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
    public String getImage() {
        return super.getImage()  + id;
    }

}
