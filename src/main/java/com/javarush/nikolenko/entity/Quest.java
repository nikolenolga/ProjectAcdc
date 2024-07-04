package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
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
@Table(name = "quest")
@ToString
@Cacheable
public class Quest implements AbstractComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_author_id")
    @ToString.Exclude
    private User author;

    @ManyToOne
    @JoinColumn(name = "first_question_id")
    private Question firstQuestion;

    @Column(name = "description")
    @ToString.Exclude
    private String description;

    @OneToMany(mappedBy = "quest")
    @ToString.Exclude
    private final List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        question.setQuest(this);
        questions.add(question);
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return Objects.equals(id, quest.id);
    }

    @Override
    public int hashCode() {
        return 7;
    }
}
