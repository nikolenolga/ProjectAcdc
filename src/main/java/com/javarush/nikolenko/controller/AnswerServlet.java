package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.AnswerService;
import com.javarush.nikolenko.service.GameService;
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
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {UrlHelper.ANSWER})
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
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        if (optionalAnswer.isEmpty()) {
            throw new QuestException("Answer not found");
        }
        Answer answer = optionalAnswer.get();
        req.setAttribute(Key.ANSWER, answer);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(UrlHelper.getJspPath(UrlHelper.ANSWER));
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectAddress = UrlHelper.INDEX;
        if (req.getParameter(Key.BUTTON_NEXT) != null) {
            redirectAddress = UrlHelper.QUESTION;
        }
        if(req.getParameter(Key.BUTTON_RESTART) != null) {
            HttpSession session = req.getSession(false);
            long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);
            gameService.restartGame(gameId);
            redirectAddress = UrlHelper.QUESTION;
        }
        if (req.getParameter(Key.BUTTON_QUESTS) != null) {
            redirectAddress = UrlHelper.QUESTS;
        }
        resp.sendRedirect(redirectAddress);
    }
}
