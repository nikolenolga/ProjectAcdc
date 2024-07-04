package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.service.QuestService;
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

@WebServlet(urlPatterns = {UrlHelper.ADD_QUESTION})
public class AddQuestionServlet extends HttpServlet {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = NanoSpring.find(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));
        req.setAttribute(Key.QUEST_ID, questId);
        String jspPath = UrlHelper.getJspPath(UrlHelper.ADD_QUESTION);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        String redirectPath = UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, questId);

        if (req.getParameter(Key.BUTTON_ADD_QUESTION) != null) {
            String questionMessage = req.getParameter(Key.QUESTION_MESSAGE);
            questService.addNewQuestionToCreatedQuest(questId, questionMessage);
        }

        resp.sendRedirect(redirectPath);
    }
}
