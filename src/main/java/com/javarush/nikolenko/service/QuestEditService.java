package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.*;
import com.javarush.nikolenko.entity.*;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

import static com.javarush.nikolenko.utils.Key.*;

@Slf4j
@AllArgsConstructor
@Transactional
public class QuestEditService {
    private final QuestService questService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ImageService imageService;
    //private final ButtonService buttonService;
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void uploadQuestImage(HttpServletRequest req) throws ServletException, IOException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));
        Quest quest = questRepository.get(questId).get();
        imageService.uploadImage(req, quest.getImage());
    }

    public void uploadQuestionImage(HttpServletRequest req) throws ServletException, IOException {
        long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
        Question question = questionRepository.get(questionId).get();
        imageService.uploadImage(req, question.getImage());
    }

    public void uploadAnswerImage(HttpServletRequest req) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        Answer answer = answerRepository.get(answerId).get();
        imageService.uploadImage(req, answer.getImage());
    }

    public void deleteQuestion(HttpServletRequest req) {
        long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
        questionRepository.delete(questionId);
    }

    public void deleteAnswer(HttpServletRequest req) {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        answerRepository.delete(answerId);
    }

    public void updateQuest(HttpServletRequest req) {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));
        String name = req.getParameter(Key.NAME);
        String description = req.getParameter(Key.DESCRIPTION);
        long firstQuestionId = RequestHelper.getLongValue(req, Key.FIRST_QUESTION_ID);
        questService.updateQuest(questId, name, description, firstQuestionId);
    }

    public void updateQuestion(HttpServletRequest req) {
        long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
        String questionMessage = req.getParameter(Key.QUESTION_MESSAGE);
        questionService.updateQuestion(questionId, questionMessage);
    }

    public void updateAnswer(HttpServletRequest req) {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        String answerMessage = req.getParameter(Key.ANSWER_MESSAGE);
        GameState gameState = GameState.valueOf(req.getParameter(Key.GAMESTATE));
        long nextQuestionId = RequestHelper.getLongValue(req, Key.NEXT_QUESTION_ID);
        String finalMessage = req.getParameter(Key.FINAL_MESSAGE);
        answerService.updateAnswer(answerId, answerMessage, gameState, nextQuestionId, finalMessage);
    }

    public void editQuest(HttpServletRequest req) throws ServletException, IOException {
        ButtonService buttonService = NanoSpring.find(ButtonService.class);
        Optional<String> operationKeyOptional = QUEST_EDIT_BUTTONS
                .stream()
                .map(req::getParameter)
                .filter(Objects::nonNull)
                .findFirst();

        if(operationKeyOptional.isPresent()) {
            String operationKey = operationKeyOptional.get();
            QuestEditOperation operation = buttonService.getOperation(operationKey);
            operation.execute(req);
        }
    }

    public Optional<QuestTo> parseQuest(UserTo authorTo, String text) {
        //parse app entities from text
        User author = Dto.MAPPER.from(authorTo);
        StringBuilder name = new StringBuilder();
        StringBuilder description = new StringBuilder();
        Map<Long, Question> questions = new TreeMap<>();
        Map<Long, List<Answer>> answers = new TreeMap<>();
        Question currentQuestion = null;
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(": name") || line.startsWith(":name")) {
                    if (!name.isEmpty()) name.append("\r\n");
                    name.append(line.substring(6).replace(":", "").trim());
                } else if (line.startsWith(": description") || line.startsWith(":description")) {
                    if (!description.isEmpty()) description.append("\r\n");
                    description.append(line.substring(13).replace(":", "").trim());
                } else if (line.startsWith("?")) {
                    //question sample line - ? : questionId : questionMessage
                    String[] questionLine = line.split(":");
                    long questionId = Long.parseLong(questionLine[1].trim());
                    String questionMessage = questionLine[2].trim();
                    Question question = Question.builder()
                            .questionMessage(questionMessage).build();
                    questions.put(questionId, question);
                    currentQuestion = question;
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
                            .question(currentQuestion)
                            .answerMessage(answerText)
                            .finalMessage(finalMessage)
                            .build();

                    if(nextQuestionId != 0L) {
                        if(!answers.containsKey(nextQuestionId)) {
                            answers.put(nextQuestionId, new ArrayList<>());
                        }
                        answers.get(nextQuestionId).add(answer);
                    }
                }
            }

            Quest quest = Quest.builder()
                    .name(name.toString())
                    .author(author)
                    .firstQuestion(questions.get(1L))
                    .description(description.toString())
                    .build();
            Optional<Quest> optionalQuest = questRepository.create(quest);

            if(optionalQuest.isEmpty()) throw new QuestException("Quest creation failed");

            //save questions to repo & set quest firstQuestionId
            for (Map.Entry<Long, Question> entry : questions.entrySet()) {
                Long key = entry.getKey();
                Question question = entry.getValue();
                question.setQuest(quest);
                if (questionRepository.create(question).isEmpty()) {
                    log.error("Question creation failed - {}", question);
                    throw new QuestException(Key.WRONG_SYNTAX_QUESTION);
                }
                for(Answer answer : answers.get(key)) {
                    answer.setNextQuestion(question);
                    if (answerRepository.create(answer).isEmpty()) {
                        log.error("Answer creation failed - {}", answer);
                        throw new QuestException(Key.WRONG_SYNTAX_ANSWER);
                    }
                }
            }

            //questRepository.update(quest);
            log.debug("Quest {} loaded", optionalQuest.orElse(null));
            return optionalQuest.map(Dto.MAPPER::from);
        } catch (Exception e) {
            log.error("Quest {} loading failed: {}", name, e.getMessage());
        }

        return Optional.empty();
    }

    public void loadQuest(UserTo authorTo, String path) {
        String text = loadTextFromFile(path);
        parseQuest(authorTo, text);
    }

    public String loadTextFromFile(String sPath) {
        StringBuilder fileText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(sPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileText.append(line).append("\r\n");
            }
        } catch (FileNotFoundException e) {
            log.error("Loading text failed, can't find file {}", sPath);
            throw new QuestException(Key.FILE_NOT_FOUND);
        } catch (IOException e) {
            log.error("Can't load file {}", sPath);
            throw new QuestException(Key.FILE_LOAD_ERROR);
        }
        return fileText.toString();
    }

//    public void editQuest(HttpServletRequest req, HttpServletResponse resp) {
//        //quest-edit
//        if (req.getParameter(Key.BUTTON_LOAD_QUEST_IMAGE) != null) {
//            uploadQuestImage(req, questId);
//        } else if (req.getParameter(Key.BUTTON_EDIT_QUEST) != null) {
//            String name = req.getParameter(Key.NAME);
//            String description = req.getParameter(Key.DESCRIPTION);
//            long firstQuestionId = RequestHelper.getLongValue(req, Key.FIRST_QUESTION_ID);
//            updateQuest(questId, name, description, firstQuestionId);
//        }
//
//        //question-edit
//        if (req.getParameter(Key.BUTTON_DELETE_QUESTION) != null) {
//            deleteQuestion(questionId);
//        } else if (req.getParameter(Key.BUTTON_EDIT_QUESTION) != null) {
//            String questionMessage = req.getParameter(Key.QUESTION_MESSAGE);
//            updateQuestion(questId, questionId, questionMessage);
//        } else if (req.getParameter(Key.BUTTON_LOAD_QUESTION_IMAGE) != null) {
//            uploadQuestionImage(req, questionId);
//        }
//
//        //answer-edit
//        if (req.getParameter(Key.BUTTON_DELETE_ANSWER) != null) {
//            deleteAnswer(answerId);
//        } else if (req.getParameter(Key.BUTTON_EDIT_ANSWER) != null) {
//            String answerMessage = req.getParameter(Key.ANSWER_MESSAGE);
//            GameState gameState = GameState.valueOf(req.getParameter(Key.GAMESTATE));
//            long nextQuestionId = RequestHelper.getLongValue(req, Key.NEXT_QUESTION_ID);
//            String finalMessage = req.getParameter(Key.FINAL_MESSAGE);
//            updateAnswer(questId, questionId, answerId, answerMessage, gameState, nextQuestionId, finalMessage);
//        } else if (req.getParameter(Key.BUTTON_LOAD_ANSWER_IMAGE) != null) {
//            uploadAnswerImage(req, answerId);
//        }
//    }

}
