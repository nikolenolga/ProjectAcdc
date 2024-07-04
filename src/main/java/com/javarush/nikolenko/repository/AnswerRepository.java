package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;

public class AnswerRepository extends BaseRepository<Answer> {

    public AnswerRepository(SessionCreater sessionCreater) {
        super(sessionCreater, Answer.class);
    }

}
