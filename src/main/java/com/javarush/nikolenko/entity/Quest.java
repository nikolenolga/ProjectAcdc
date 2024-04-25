package com.javarush.nikolenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class Quest extends AbstractComponent {
    private final List<Question> questions = new ArrayList<>();
    private String name;
    private Long userAuthorId;
    private Long firstQuestionId;
    private String description;

    public Quest(String name, long userAuthorId, long firstQuestionId, String description) {
        super(0L);
        this.name = name;
        this.userAuthorId = userAuthorId;
        this.firstQuestionId = firstQuestionId;
        this.description = description;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}
