package com.javarush.nikolenko.mapping;

import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Dto {
    Dto MAPPER = Mappers.getMapper(Dto.class);

    @Mapping(target = "image", source = "image")
    UserTo from(User user);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "firstQuestionId", source = "firstQuestion.id")
    @Mapping(target = "image", source = "image")
    QuestTo from(Quest quest);

    @Mapping(target = "questId", source = "quest.id")
    @Mapping(target = "image", source = "image")
    QuestionTo from(Question question);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "nextQuestionId", source = "nextQuestion.id")
    @Mapping(target = "image", source = "image")
    AnswerTo from(Answer answer);

    @Mapping(target = "questId", source = "quest.id")
    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "currentQuestionId", source = "currentQuestion.id")
    GameTo from(Game game);


    User from(UserTo userTo);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "firstQuestion.id", source = "firstQuestionId")
    Quest from(QuestTo questTo);

    @Mapping(target = "quest.id", source = "questId")
    Question from(QuestionTo questionTo);

    @Mapping(target = "nextQuestion.id", source = "nextQuestionId")
    @Mapping(target = "question.id", source = "questionId")
    Answer from(AnswerTo answerTo);

    @Mapping(target = "quest.id", source = "questId")
    @Mapping(target = "player.id", source = "playerId")
    @Mapping(target = "currentQuestion.id", source = "currentQuestionId")
    Game from(GameTo gameTo);

}
