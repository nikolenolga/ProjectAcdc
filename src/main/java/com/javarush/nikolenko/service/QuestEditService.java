package com.javarush.nikolenko.service;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.AnswerRepository;
import com.javarush.nikolenko.repository.QuestRepository;
import com.javarush.nikolenko.repository.QuestionRepository;
import com.javarush.nikolenko.repository.UserRepository;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static com.javarush.nikolenko.utils.Key.QUEST_EDIT_BUTTONS;

@Slf4j
@AllArgsConstructor
@Transactional
public class QuestEditService {
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestService questService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ImageService imageService;


    public void uploadQuestImage(HttpServletRequest req) throws ServletException, IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        Quest quest = questRepository.get(questId).orElseThrow();
        imageService.uploadImage(req, quest.getImage());
    }

    public void uploadQuestionImage(HttpServletRequest req) throws ServletException, IOException {
        long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
        Question question = questionRepository.get(questionId).orElseThrow();
        imageService.uploadImage(req, question.getImage());
    }

    public void uploadAnswerImage(HttpServletRequest req) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        Answer answer = answerRepository.get(answerId).orElseThrow();
        imageService.uploadImage(req, answer.getImage());
    }

    public void deleteQuestion(HttpServletRequest req) {
        long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);

        Optional<Question> question = questionRepository.get(questionId);
        Optional<Quest> quest = questRepository.get(questId);

        if(question.isEmpty() || quest.isEmpty()) {
            req.setAttribute(Key.ALERT, "Can't find current quest entities");
            throw new QuestException("Can't find current quest entities");
        }
        if(question.get().equals(quest.get().getFirstQuestion())) {
            req.setAttribute(Key.ALERT, "Can't delete quest first question. Declare new quest first question before deleting.");
            throw new QuestException("Can't delete quest first question. Declare new quest first question before deleting.");
        }
        questionRepository.delete(questionId);
    }

    public void deleteAnswer(HttpServletRequest req) {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        answerRepository.delete(answerId);
    }

    public void updateQuest(HttpServletRequest req) {
        long questId =  RequestHelper.getLongValue(req, Key.QUEST_ID);
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

    public void editQuest(HttpServletRequest req) throws QuestException, ServletException, IOException {
        ButtonService buttonService = NanoSpring.find(ButtonService.class);
        Optional<String> operationKeyOptional = QUEST_EDIT_BUTTONS
                .stream()
                .filter(value -> req.getParameter(value) != null)
                .findFirst();

        if(operationKeyOptional.isPresent()) {
            String operationKey = operationKeyOptional.get();
            QuestEditOperation operation = buttonService.getOperation(operationKey);
            operation.execute(req);
        }
    }

    public Optional<QuestTo> parseQuest(long authorId, String text) throws NoSuchElementException{
        //parse app entities from text
        User author = userRepository.get(authorId).orElseThrow();
        StringBuilder name = new StringBuilder();
        StringBuilder description = new StringBuilder();

        Quest quest = Quest.builder()
                .build();
        questRepository.create(quest).orElseThrow();

        author.addQuest(quest);

        Map<Long, Question> questions = new HashMap<>();
        Map<Long, List<Answer>> answers = new HashMap<>();
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
                            .questionMessage(questionMessage)
                            .build();
                    questionRepository.create(question).orElseThrow();
                    quest.addQuestion(question);

                    currentQuestion = question;
                    questions.put(questionId, question);
                } else if (line.startsWith("=") || line.startsWith(">") || line.startsWith("<")) {
                    //answer sample line - gameState : nextQuestionId : answerText : finalMessage
                    if(currentQuestion == null) throw new QuestException("Answer can't be created without question");

                    String[] answerLine = line.split(":");
                    String state = answerLine[0].trim();
                    GameState gameState = "=".equals(state)
                            ? GameState.GAME
                            : ">".equals(state)
                            ? GameState.WIN
                            : GameState.LOSE;
                    Long nextQuestionId = Long.parseLong(answerLine[1].trim());
                    String answerText = answerLine[2].trim();
                    String finalMessage = answerLine.length > 3 ? answerLine[3].trim() : "";

                    Answer answer = Answer.builder()
                            .gameState(gameState)
                            .answerMessage(answerText)
                            .finalMessage(finalMessage)
                            .build();
                    answerRepository.create(answer).orElseThrow();

                    currentQuestion.addPossibleAnswer(answer);

                    if(nextQuestionId != 0L) {
                        if(!answers.containsKey(nextQuestionId)) {
                            answers.put(nextQuestionId, new ArrayList<>());
                        }
                        answers.get(nextQuestionId).add(answer);
                    }
                }
            }

            if(!questions.containsKey(1L)) throw new QuestException("First Question not found. Quest can't be created");

            quest.setName(name.toString());
            quest.setFirstQuestion(questions.get(1L));
            quest.setDescription(description.toString());

            //set answers nextQuestions
            for (Map.Entry<Long, List<Answer>> entry : answers.entrySet()) {
                Long key = entry.getKey();
                if(!questions.containsKey(key)) throw new QuestException("Answer nextQuestion was not added to questions list");
                entry.getValue().forEach(answer -> answer.setNextQuestion(questions.get(key)));
            }


            log.debug("Quest {} loaded", quest.getName());
            return Optional.of(quest).map(Dto.MAPPER::from);
        } catch (Exception e) {
            log.error("Quest {} loading failed: {}", name, e.getMessage());
            throw new QuestException("Quest %s loading failed: %s".formatted(quest.getName(), e.getMessage()));
        }
    }

    public void loadQuest(long authorId, String path) {
        String text = loadTextFromFile(path);
        try {
            parseQuest(authorId, text);
        } catch (Exception e) {
            log.error("Quests parsing from path {}, caused exception - {}", path, e.getMessage());
            throw new QuestException("Can't parse current quest %s, message %s".formatted(path, e.getMessage()));
        }
    }

    public String loadTextFromFile(String sPath) {
        String fileText;
        try {
            Path path = Paths.get(sPath);
            List<String> fileTextLines = Files.readAllLines(path);
            fileText = String.join("\n", fileTextLines);
        } catch (FileNotFoundException e) {
            log.error("Loading text failed, can't find file {}", sPath);
            throw new QuestException(Key.FILE_NOT_FOUND);
        } catch (IOException e) {
            log.error("Can't load file {}", sPath);
            throw new QuestException(Key.FILE_LOAD_ERROR);
        }
        return fileText;
    }

}
