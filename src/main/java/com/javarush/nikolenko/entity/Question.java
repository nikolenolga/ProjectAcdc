package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
@ToString
@Cacheable
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

    @OneToMany(mappedBy = "question")
    @ToString.Exclude
    private final List<Answer> possibleAnswers = new ArrayList<>();

    public List<Answer> getPossibleAnswers() {
        return Collections.unmodifiableList(possibleAnswers);
    }

    public void addPossibleAnswer(Answer answer) {
        answer.setQuestion(this);
        possibleAnswers.add(answer);
    }

    public void removePossibleAnswer(Answer answer) {
        possibleAnswers.remove(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return 15;
    }
}
