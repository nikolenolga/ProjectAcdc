package com.javarush.nikolenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
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
        this.name = name;
        this.userAuthorId = userAuthorId;
        this.firstQuestionId = firstQuestionId;
        this.description = description;
        log.debug("New Quest entity created, id - {}, name - {}, authorId - {}", id, name, userAuthorId);
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
