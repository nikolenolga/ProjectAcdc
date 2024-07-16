package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.config.SessionCreater;
import org.hibernate.Session;

import java.util.Collection;

public class QuestionRepository extends BaseRepository<Question> {

    public QuestionRepository(SessionCreater sessionCreater) {
        super(sessionCreater, Question.class);
    }

    public Collection<Answer> getAnswersByQuestionId(long id) {
        Session session = sessionCreater.getSession();
        Question question = get(id).orElseThrow();
        return question.getPossibleAnswers();
    }
}
