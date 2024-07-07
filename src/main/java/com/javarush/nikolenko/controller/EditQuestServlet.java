package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.GameState;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestEditService;
import com.javarush.nikolenko.service.QuestService;
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
import java.util.Collection;

@MultipartConfig
@WebServlet(urlPatterns = {UrlHelper.EDIT_QUEST})
public class EditQuestServlet extends HttpServlet {
    private QuestEditService questEditService;
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = NanoSpring.find(QuestService.class);
        questEditService = NanoSpring.find(QuestEditService.class);
        config.getServletContext().setAttribute(Key.GAMESTATES, GameState.values());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        QuestTo quest = questService.getQuestWithQuestions(questId);

        req.setAttribute(Key.QUESTIONS, quest.getQuestions());
        req.setAttribute(Key.QUEST, quest);
        req.setAttribute(Key.QUEST_ID, questId);

        String jspPath = UrlHelper.getJspPath(UrlHelper.EDIT_QUEST);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws QuestException, IOException, ServletException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));

        questEditService.editQuest(req);

        resp.sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, questId));
    }
}
