package com.javarush.nikolenko.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question extends AbstractComponent {
    private String questionMessage;
    private final List<Answer> possibleAnswers = new ArrayList<>();

    public Question() {}

    public Question(String questionMessage) {
        super(0L);
        this.questionMessage = questionMessage;
    }

    public List<Answer> getPossibleAnswers() {
        return Collections.unmodifiableList(possibleAnswers);
    }

    public void addPossibleAnswer(Answer answer) {
        possibleAnswers.add(answer);
    }

    public String getQuestionMessage() {
        return questionMessage;
    }

    public void setQuestionMessage(String questionMessage) {
        this.questionMessage = questionMessage;
    }
}
