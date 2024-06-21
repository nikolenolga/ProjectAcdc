package com.javarush.nikolenko.mapping;

import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Dto {
    Dto MAPPER = Mappers.getMapper(Dto.class);

    //методы для преобразования entity в dto
    UserTo from(User user);

    @Mapping(target = "authorId", source = "author.id")
    QuestTo from(Quest quest);

    @Mapping(target = "questId", source = "quest.id")
    QuestionTo from(Question question);

    @Mapping(target = "questionId", source = "question.id")
    AnswerTo from(Answer answer);

    @Mapping(target = "questId", source = "quest.id")
    @Mapping(target = "playerId", source = "player.id")
    GameTo from(Game game);


    //методы для преобразования dto в entity
    User from(UserTo userTo);

    @Mapping(target = "author.id", source = "authorId")
    Quest from(QuestTo questTo);

    @Mapping(target = "quest.id", source = "questId")
    Question from(QuestionTo questionTo);

    @Mapping(target = "question.id", source = "questionId")
    Answer from(AnswerTo answerTo);

    @Mapping(target = "quest.id", source = "questId")
    @Mapping(target = "player.id", source = "playerId")
    Game from(GameTo gameTo);

}
