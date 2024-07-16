package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "question", schema = "public")
public class Question implements AbstractComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_message")
    private String questionMessage;

    @ManyToOne
    @JoinColumn(name = "quest_id")
    @ToString.Exclude
    private Quest quest;

    @OneToMany
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    private final List<Answer> possibleAnswers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id != null && Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return 15;
    }

    public List<Answer> getPossibleAnswers() {
        return Collections.unmodifiableList(possibleAnswers);
    }

    public void addPossibleAnswer(Answer answer) {
        answer.setQuestion(this);
        possibleAnswers.add(answer);
    }
}
