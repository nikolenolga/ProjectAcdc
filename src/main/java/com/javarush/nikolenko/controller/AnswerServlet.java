package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.AnswerService;
import com.javarush.nikolenko.service.GameService;
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

@WebServlet(urlPatterns = {"/answer"})
public class AnswerServlet extends HttpServlet {
    private GameService gameService;
    private AnswerService answerService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        gameService = ServiceLocator.getService(GameService.class);
        answerService = ServiceLocator.getService(AnswerService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, "answerId");
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        if (optionalAnswer.isEmpty()) {
            throw new QuestException("Answer not found");
        }
        Answer answer = optionalAnswer.get();
        req.setAttribute("answer", answer);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("WEB-INF/views/answer.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectAddress = "";
        if (req.getParameter("next") != null) {
            redirectAddress = "question";
        }
        if(req.getParameter("button-again") != null) {
            HttpSession session = req.getSession(false);
            long gameId = RequestHelper.getLongValue(session, "gameId");
            gameService.restartGame(gameId);
            redirectAddress = "question";
        }
        if (req.getParameter("button-quests") != null) {
            redirectAddress = "quests";
        }
        resp.sendRedirect(redirectAddress);
    }
}
