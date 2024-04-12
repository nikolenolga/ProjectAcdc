package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.Configuration;
import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.utils.RequestHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/game-start")
public class GameStartServlet extends HttpServlet {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = ServiceLocator.getService(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = RequestHelper.getLongValue(req, "questId");
        Optional<Quest> optionalQuest = questService.get(questId);
        if (optionalQuest.isEmpty()) {
            throw new QuestException("Quest not found");
        }
        HttpSession session = req.getSession();
        Quest quest = optionalQuest.get();
        session.setAttribute("quest", quest);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("WEB-INF/game-start.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = RequestHelper.getLongValue(req, "questId");
        long currentQuestionId = questService.getCurrentQuestionId(questId);
        if (currentQuestionId == 0) {
            throw new QuestException("First quest question not found");
        }
        resp.sendRedirect("question?currentQuestionId=" + currentQuestionId);
    }
}
