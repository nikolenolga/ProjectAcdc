package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.service.*;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;

@MultipartConfig
@WebServlet(urlPatterns={UrlHelper.EDIT_QUEST})
public class EditQuestServlet extends HttpServlet {
    private QuestModifyService questModifyService;
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = ServiceLocator.getService(QuestService.class);
        questModifyService = ServiceLocator.getService(QuestModifyService.class);
        config.getServletContext().setAttribute(Key.GAMESTATES, GameState.values());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        Quest quest = questService.get(questId).get();

        req.setAttribute(Key.QUESTIONS, quest.getQuestions());
        req.setAttribute(Key.QUEST, quest);
        req.setAttribute(Key.QUEST_ID, questId);

        String jspPath = UrlHelper.getJspPath(UrlHelper.EDIT_QUEST);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));
        long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);

        //quest-edit
        if(req.getParameter(Key.BUTTON_LOAD_QUEST_IMAGE) != null){
            questModifyService.uploadQuestImage(req, questId);
        } else if(req.getParameter(Key.BUTTON_EDIT_QUEST) != null) {
            String name = req.getParameter(Key.NAME);
            String description = req.getParameter(Key.DESCRIPTION);
            long firstQuestionId = RequestHelper.getLongValue(req, Key.FIRST_QUESTION_ID);
            questModifyService.updateQuest(questId, name, description, firstQuestionId);
        }

        //question-edit
        if(req.getParameter(Key.BUTTON_DELETE_QUESTION) != null) {
            questModifyService.deleteQuestion(questId, questionId);
        } else if(req.getParameter(Key.BUTTON_EDIT_QUESTION) != null) {
            String questionMessage = req.getParameter(Key.QUESTION_MESSAGE);
            questModifyService.updateQuestion(questId, questionId, questionMessage);
        } else if(req.getParameter(Key.BUTTON_LOAD_QUESTION_IMAGE) != null){
            questModifyService.uploadQuestionImage(req, questionId);
        }

        //answer-edit
        if(req.getParameter(Key.BUTTON_DELETE_ANSWER) != null) {
            questModifyService.deleteAnswer(questId, questionId, answerId);
        } else if(req.getParameter(Key.BUTTON_EDIT_ANSWER) != null) {
            String answerMessage = req.getParameter(Key.ANSWER_MESSAGE);
            GameState gameState = GameState.valueOf(req.getParameter(Key.GAMESTATE));
            long nextQuestionId = RequestHelper.getLongValue(req, Key.NEXT_QUESTION_ID);
            String finalMessage = req.getParameter(Key.FINAL_MESSAGE);
            questModifyService.updateAnswer(questId, questionId, answerId, answerMessage, gameState, nextQuestionId, finalMessage);
        } else if(req.getParameter(Key.BUTTON_LOAD_ANSWER_IMAGE) != null){
            questModifyService.uploadAnswerImage(req, answerId);
        }

        resp.sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, questId));
    }
}
