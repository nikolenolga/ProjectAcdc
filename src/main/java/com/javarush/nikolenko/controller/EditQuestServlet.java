package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.AnswerService;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.service.QuestionService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.Key;
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
import java.util.Collection;
import java.util.Optional;

@WebServlet(urlPatterns={UrlHelper.EDIT_QUEST})
public class EditQuestServlet extends HttpServlet {
    private QuestService questService;
    private QuestionService questionService;
    private AnswerService answerService;
    private UserService userService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = ServiceLocator.getService(QuestService.class);
        questionService = ServiceLocator.getService(QuestionService.class);
        answerService = ServiceLocator.getService(AnswerService.class);
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));
        Quest quest = questService.get(questId).get();

        req.setAttribute(Key.GAMESTATES, GameState.values());
        req.setAttribute(Key.QUESTIONS, quest.getQuestions());
        req.setAttribute(Key.QUEST, quest);
        String jspPath = UrlHelper.getJspPath(UrlHelper.EDIT_QUEST);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }
}
