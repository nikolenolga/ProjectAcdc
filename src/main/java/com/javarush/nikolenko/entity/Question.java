package com.javarush.nikolenko.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractComponent {
    private final List<Answer> possibleAnswers = new ArrayList<>();
    private Long id;
    private String questionMessage;
    private Long questId;

    public Question(String questionMessage) {
        this.questionMessage = questionMessage;
    }

    public Question(String questionMessage, Long questId) {
        this.questionMessage = questionMessage;
        this.questId = questId;
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

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }
}
