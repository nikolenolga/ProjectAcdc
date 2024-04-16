package com.javarush.nikolenko.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quest extends AbstractComponent {
    private String name;
    private long userAuthorId;
    private final List<Question> questions = new ArrayList<>();
    private long firstQuestionId;
    private String description;

    public Quest() {};

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

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public String getName() {
        return name;
    }

    public long getFirstQuestionId() {
        return firstQuestionId;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstQuestionId(long firstQuestionId) {
        this.firstQuestionId = firstQuestionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUserAuthorId() {
        return userAuthorId;
    }

    public void setUserAuthorId(long userAuthorId) {
        this.userAuthorId = userAuthorId;
    }
}
