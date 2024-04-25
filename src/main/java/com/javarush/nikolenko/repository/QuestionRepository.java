package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class QuestionRepository extends BaseRepository<Question> {
    public Collection<Answer> getAnswersByQuestionId(long id) {
        Collection<Answer> answers;
        Optional<Question> optionalQuestion = get(id);
        answers = optionalQuestion.isPresent()
                ? optionalQuestion.get().getPossibleAnswers()
                : new ArrayList<>();
        return answers;
    }
}
