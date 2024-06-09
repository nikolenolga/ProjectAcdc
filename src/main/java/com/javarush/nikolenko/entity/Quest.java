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
@Table(name = "quest")
@ToString
@NamedQueries({
        @NamedQuery(name = "QUERY_MORE_ID", query = "SELECT q FROM Quest q where id>:id")
})
public class Quest extends AbstractComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_author_id")
    @ToString.Exclude
    private User author;

    @Column(name = "first_question_id")
    private Long firstQuestionId;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "quest")
    @ToString.Exclude
    private final List<Question> questions = new ArrayList<>();


    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    @Override
    public String getImage() {
        return super.getImage()  + id;
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
