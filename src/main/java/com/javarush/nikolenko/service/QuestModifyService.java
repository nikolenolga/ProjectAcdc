package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.exception.QuestException;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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

    public Optional<Quest> parseQuest(long userId, String text) {
        //parse app entities from text
        StringBuilder name = new StringBuilder();
        StringBuilder description = new StringBuilder();
        Map<Long, Question> questions = new TreeMap<>();
        long currentQuestionIndex = 0;
        try(BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null){
                if (line.startsWith(": name") || line.startsWith(":name")) {
                    if(!name.isEmpty()) name.append("\r\n");
                    name.append(line.substring(6).replace(":", "").trim());
                } else if (line.startsWith(": description") || line.startsWith(":description")) {
                    if(!description.isEmpty()) description.append("\r\n");
                    description.append(line.substring(13).replace(":", "").trim());
                } else if (line.startsWith("?")) {
                    //question sample line - ? : questionId : questionMessage
                    String[] questionLine = line.split(":");
                    long questionId = Long.parseLong(questionLine[1].trim());
                    String questionMessage = questionLine[2].trim();
                    Question question = Question.builder()
                            .questionMessage(questionMessage).build();
                    questions.put(questionId, question);
                    currentQuestionIndex = questionId;
                } else if (line.startsWith("=") || line.startsWith(">") || line.startsWith("<")) {
                    //answer sample line - gameState : nextQuestionId : answerText : finalMessage
                    String[] answerLine = line.split(":");
                    String state = answerLine[0].trim();
                    GameState gameState = "=".equals(state)
                            ? GameState.GAME
                            : ">".equals(state)
                            ? GameState.WIN
                            : GameState.LOSE;
                    long nextQuestionId = Long.parseLong(answerLine[1].trim());
                    String answerText = answerLine[2].trim();
                    String finalMessage = answerLine.length > 3 ? answerLine[3].trim() : "";

                    Answer answer = Answer.builder()
                            .gameState(gameState)
                            .nextQuestionId(nextQuestionId)
                            .answerMessage(answerText)
                            .finalMessage(finalMessage)
                            .build();
                    questions.get(currentQuestionIndex).addPossibleAnswer(answer);
                }
            }

            Quest quest = Quest.builder()
                    .name(name.toString())
                    .userAuthorId(userId)
                    .firstQuestionId(1L)
                    .description(description.toString())
                    .build();

            //save questions to repo & set quest firstQuestionId
            for (Map.Entry<Long, Question> entry : questions.entrySet()) {
                Question question = entry.getValue();
                if (questionService.create(question).isEmpty()) {
                    throw new QuestException("Question line wrong syntax");
                }
                if (entry.getKey() == 1L) {
                    quest.setFirstQuestionId(question.getId());
                }
                quest.addQuestion(question);
            }

            //set answers nextQuestionId & save answers to repo & question update with changed answers list
            for (Question question : questions.values()) {
                for (Answer answer : question.getPossibleAnswers()) {
                    long parsedQuestionId = answer.getNextQuestionId();
                    if(parsedQuestionId != 0L) {
                        Question nextQuestion = questions.get(parsedQuestionId);
                        long savedQuestionId = nextQuestion.getId();
                        answer.setNextQuestionId(savedQuestionId);
                    }
                    if (answerService.create(answer).isEmpty()) {
                        throw new QuestException("Answer line wrong syntax");
                    }
                }
                questionService.update(question);
            }

            Optional<Quest> optionalQuest = questService.create(quest);
            return optionalQuest;
        } catch (QuestException e) {

        } catch (Exception e) {

        }

        return Optional.empty();
    }
}
