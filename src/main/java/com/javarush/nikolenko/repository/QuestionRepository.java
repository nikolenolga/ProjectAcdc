package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class QuestionRepository extends BaseRepository<Question> {

    public QuestionRepository(SessionCreater sessionCreater) {
        super(sessionCreater, Question.class);
    }

    public Collection<Answer> getAnswersByQuestionId(long id) {
        Session session = sessionCreater.getSession();
        Question question = session.find(Question.class, id);

        return question.getPossibleAnswers();
    }
}
