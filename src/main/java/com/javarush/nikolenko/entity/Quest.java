package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quest")
@ToString(exclude = {"questions"})
@NamedQueries({
        @NamedQuery(name = "QUERY_MORE_ID", query = "SELECT q FROM Quest q where id>:id")
})
public class Quest extends AbstractComponent {
    @Transient
    private final List<Question> questions = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "user_author_id")
    private Long userAuthorId;
    @Column(name = "first_question_id")
    private Long firstQuestionId;

    private String description;

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

}
