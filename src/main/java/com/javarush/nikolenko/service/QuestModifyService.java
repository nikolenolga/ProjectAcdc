package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import lombok.SneakyThrows;

import java.util.Optional;

public class QuestModifyService {
    QuestService questService;
    QuestionService questionService;
    AnswerService answerService;


    @SneakyThrows
    public QuestModifyService() {
        this.questService = ServiceLocator.getService(QuestService.class);
        this.questionService = ServiceLocator.getService(QuestionService.class);
        this.answerService = ServiceLocator.getService(AnswerService.class);
    }

    public void addQuestion(long questId, long questionId) {
        Optional<Quest> optionalQuest = questService.get(questId);
        Optional<Question> optionalQuestion = questionService.get(questionId);
        if (optionalQuest.isPresent() && optionalQuestion.isPresent()) {
            Quest quest = optionalQuest.get();
            quest.addQuestion(optionalQuestion.get());
            questService.update(quest);
        }
    }

    public void addAnswer(long questId, long questionId, long answerId) {
        Optional<Quest> optionalQuest = questService.get(questId);
        Optional<Question> optionalQuestion = questionService.get(questionId);
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        if (optionalQuest.isPresent() && optionalQuestion.isPresent() && optionalAnswer.isPresent()) {
            Quest quest = optionalQuest.get();
            Question question = optionalQuestion.get();
            Answer answer = optionalAnswer.get();
            question.addPossibleAnswer(answer);
            questionService.update(question);
            questService.update(quest);
        }
    }

    public void deleteQuest(long questId) {
        questService.get(questId).ifPresent(this::deleteQuest);
    }

    private void deleteQuest(Quest quest) {
        quest.getQuestions().forEach(this::deleteQuestion);
        questService.delete(quest);
    }

    public void deleteQuestion(long questId, long questionId) {
        Optional<Quest> optionalQuest = questService.get(questId);
        Optional<Question> optionalQuestion = questionService.get(questionId);
        if (optionalQuest.isPresent()) {
            Quest quest = optionalQuest.get();
            Question question = optionalQuestion.get();
            quest.deleteQuestion(question);
            deleteQuestion(question);
            questService.update(quest);
        }
    }

    private void deleteQuestion(Question question) {
        question.getPossibleAnswers().forEach(answer -> answerService.delete(answer));
        questionService.delete(question);
    }

    public void deleteAnswer(long questId, long questionId, long answerId) {
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        Optional<Question> optionalQuestion = questionService.get(questionId);
        Optional<Quest> optionalQuest = questService.get(questId);
        if(optionalAnswer.isPresent() && optionalQuestion.isPresent() && optionalQuest.isPresent()) {
            Answer answer = optionalAnswer.get();
            Question question = optionalQuestion.get();
            Quest quest = optionalQuest.get();

            answerService.delete(answer);
            question.removePossibleAnswer(answer);
            questionService.update(question);
            questService.update(quest);
        }
    }

    public void updateQuest(long questId, String name, String description, long firstQuestionId) {
        Optional<Quest> optionalQuest = questService.get(questId);
        if (optionalQuest.isPresent()) {
            Quest quest = optionalQuest.get();
            quest.setName(name);
            quest.setDescription(description);
            quest.setFirstQuestionId(firstQuestionId);
            questService.update(quest);
        }
    }

    public void updateQuestion(long questId, long questionId, String questionMessage) {
        Optional<Question> optionalQuestion = questionService.get(questionId);
        Optional<Quest> optionalQuest = questService.get(questId);
        if (optionalQuestion.isPresent() && optionalQuest.isPresent()) {
            Quest quest = optionalQuest.get();
            Question question = optionalQuestion.get();
            question.setQuestionMessage(questionMessage);
            questionService.update(question);
            questService.update(quest);
        }
    }

    public void updateAnswer(long questId, long questionId, long answerId,
                             String answerMessage, GameState gameState,
                             long nextQuestionId, String finalMessage) {
        Optional<Quest> optionalQuest = questService.get(questId);
        Optional<Question> optionalQuestion = questionService.get(questionId);
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        if(optionalQuest.isPresent() && optionalQuestion.isPresent() && optionalAnswer.isPresent()) {
            Quest quest = optionalQuest.get();
            Question question = optionalQuestion.get();
            Answer answer = optionalAnswer.get();

            answer.setAnswerMessage(answerMessage);
            answer.setNextQuestionId(nextQuestionId);
            answer.setFinalMessage(finalMessage);
            answer.setGameState(gameState);

            answerService.update(answer);
            questionService.update(question);
            questService.update(quest);
        }
    }
}
