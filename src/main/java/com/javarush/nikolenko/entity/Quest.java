package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
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
@Table(name = "quest", schema = "public")
public class Quest implements AbstractComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @ToString.Exclude
    private String description;

    @ManyToOne
    @JoinColumn(name = "first_question_id")
    private Question firstQuestion;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    @OneToMany
    @JoinColumn(name = "quest_id")
    @ToString.Exclude
    private final List<Question> questions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return id != null && Objects.equals(id, quest.id);
    }

    @Override
    public int hashCode() {
        return 7;
    }

    public void addQuestion(Question question) {
        question.setQuest(this);
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}
