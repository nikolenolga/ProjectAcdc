package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.io.Serial;
import java.io.Serializable;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Question implements AbstractComponent, Serializable  {
    @Serial
    private static final long serialVersionUID = -1798070784493154676L;

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

    @Version
    Long version;

    public List<Answer> getPossibleAnswers() {
        return Collections.unmodifiableList(possibleAnswers);
    }

    public void addPossibleAnswer(Answer answer) {
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
