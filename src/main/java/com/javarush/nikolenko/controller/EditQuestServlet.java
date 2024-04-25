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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import java.io.IOException;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));

        //quest-edit
        if(req.getParameter(Key.BUTTON_EDIT_QUEST) != null) {
            String name = req.getParameter(Key.NAME);
            String description = req.getParameter(Key.DESCRIPTION);
            long firstQuestionId = RequestHelper.getLongValue(req, Key.FIRST_QUESTION_ID);
            questModifyService.updateQuest(questId, name, description, firstQuestionId);
        }

        //question-edit
        if(req.getParameter(Key.BUTTON_DELETE_QUESTION) != null) {
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            questModifyService.deleteQuestion(questId, questionId);
        }
        if(req.getParameter(Key.BUTTON_EDIT_QUESTION) != null) {
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            String questionMessage = req.getParameter(Key.QUESTION_MESSAGE);
            questModifyService.updateQuestion(questId, questionId, questionMessage);
        }

        //answer-edit
        if(req.getParameter(Key.BUTTON_DELETE_ANSWER) != null) {
            long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            questModifyService.deleteAnswer(questId, questionId, answerId);
        }

        if(req.getParameter(Key.BUTTON_EDIT_ANSWER) != null) {
            long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            String answerMessage = req.getParameter(Key.ANSWER_MESSAGE);
            GameState gameState = GameState.valueOf(req.getParameter(Key.GAMESTATE));
            long nextQuestionId = RequestHelper.getLongValue(req, Key.NEXT_QUESTION_ID);
            String finalMessage = req.getParameter(Key.FINAL_MESSAGE);
            questModifyService.updateAnswer(questId, questionId, answerId, answerMessage, gameState, nextQuestionId, finalMessage);
        }

        resp.sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, questId));
    }
}
