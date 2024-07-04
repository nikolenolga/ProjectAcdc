package com.javarush.nikolenko.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionTo {
    private Long id;
    String questionMessage;
    Long questId;
    List<AnswerTo> possibleAnswers;
    String image;
}
