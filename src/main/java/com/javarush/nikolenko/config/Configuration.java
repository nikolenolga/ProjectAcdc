package com.javarush.nikolenko.config;

import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.service.AnswerService;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.service.QuestionService;

import java.lang.reflect.InvocationTargetException;

public class Configuration {
    private AnswerService answerService;
    private QuestionService questionService;
    private QuestService questService;

    public Configuration() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        answerService = ServiceLocator.getService(AnswerService.class);
        questionService = ServiceLocator.getService(QuestionService.class);
        questService = ServiceLocator.getService(QuestService.class);
        configureQuests();
    }

    public void configureQuests() {
        defaultQuest();
        exampleQuest();
    }

    private void defaultQuest() {
        Question question = new Question(1L, "Сварить суп?");
        questionService.create(question);
        Question question1 = new Question(1L, "Пожарить картошку?");
        questionService.create(question1);
        Question question2 = new Question(1L, "Выпить энергетик?");
        questionService.create(question2);

        Answer answer = new Answer(1L, "Да, сварить.", GameState.WIN, 0L, "Вы поели супа, поздравляю, голод утолен.");
        answerService.create(answer);
        Answer answer1 = new Answer(1L, "Нет, не нужно. Посмотрим что еще есть.", GameState.GAME, question1.getId(), "");
        answerService.create(answer1);
        question.addPossibleAnswer(answer);
        question.addPossibleAnswer(answer1);

        Answer answer2 = new Answer(1L, "Да, пожарить.", GameState.WIN, 0L, "Вы поели картошки, поздравляю, голод утолен.");
        answerService.create(answer2);
        Answer answer3 = new Answer(1L, "Нет, не нужно. Посмотрим что еще есть.", GameState.GAME, question2.getId(), "");
        answerService.create(answer3);
        question1.addPossibleAnswer(answer2);
        question1.addPossibleAnswer(answer3);

        Answer answer4 = new Answer(1L, "Да, выпить.", GameState.LOSE, 0L, "Вы выпили энергетик на голодный желудок и заболели.");
        answerService.create(answer4);
        Answer answer5 = new Answer(1L, "Нет, не нужно. Попью воды.", GameState.LOSE, 0L, "Вы остались голодными и не смогли работать. Вас уволили.");
        answerService.create(answer5);
        Answer answer6 = new Answer(1L, "Пожалуй я вернусь  съем картошку.", GameState.WIN, 0L, "Вы поели картошки, поздравляю, голод утолен.");
        answerService.create(answer6);
        question2.addPossibleAnswer(answer4);
        question2.addPossibleAnswer(answer5);
        question2.addPossibleAnswer(answer6);

        Quest quest = new Quest(1L, "Не останься голодным.", question.getId(), "Вы пришли домой после тяжелого рабочего дня, вам срочно исправить ошибку в проекте, но вы очень голодны.");
        quest.addQuestion(question);
        quest.addQuestion(question1);
        quest.addQuestion(question2);
        questService.create(quest);
    }

    private void exampleQuest() {
        Question question = new Question(1L, "Будильник зазвонил. Пора вставать?");
        questionService.create(question);
        Question question1 = new Question(1L, "Перевести будильник?");
        questionService.create(question1);

        Answer answer = new Answer(1L, "Встать.", GameState.WIN, 0L, "Вы проснулись.");
        answerService.create(answer);
        Answer answer1 = new Answer(1L, "Нет, еще немного полежу и встану.", GameState.GAME, question1.getId(), "");
        answerService.create(answer1);
        question.addPossibleAnswer(answer);
        question.addPossibleAnswer(answer1);

        Answer answer2 = new Answer(1L, "Да, на 10 минут.", GameState.GAME, question.getId(), "");
        answerService.create(answer2);
        Answer answer3 = new Answer(1L, "Нет, не нужно. Уже встаю.", GameState.LOSE, 0L, "Ты все проспал.");
        answerService.create(answer3);
        question1.addPossibleAnswer(answer2);
        question1.addPossibleAnswer(answer3);

        Quest quest = new Quest(1L, "Example. Будильник.", question.getId(), "Утро, звенит будильник. Как бы не проспать...");
        quest.addQuestion(question);
        quest.addQuestion(question1);
        questService.create(quest);
    }

}
