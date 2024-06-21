package com.javarush.nikolenko.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestTo {
    Long id;
    String name;
    Long authorId;
    Long firstQuestionId;
    String description;
    List<QuestionTo> questions;
}
