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
public class Question extends AbstractComponent {
    private final List<Answer> possibleAnswers = new ArrayList<>();
    private String questionMessage;

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

    public void removePossibleAnswer(Answer answer) {
        possibleAnswers.remove(answer);
    }
}
