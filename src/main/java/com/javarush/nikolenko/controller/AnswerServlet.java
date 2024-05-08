package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
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

@WebServlet(urlPatterns = {UrlHelper.ANSWER})
public class AnswerServlet extends HttpServlet {
    private GameService gameService;
    private AnswerService answerService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        gameService = NanoSpring.find(GameService.class);
        answerService = NanoSpring.find(AnswerService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);

        answerService.get(answerId).ifPresent(answer -> req.setAttribute(Key.ANSWER, answer));
        gameService.checkAnswer(answerId, req);

        String jspPath = UrlHelper.getJspPath(UrlHelper.ANSWER);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String redirectAddress = UrlHelper.INDEX;
        if (req.getParameter(Key.BUTTON_NEXT) != null) {
            redirectAddress = UrlHelper.QUESTION;
        } else if (req.getParameter(Key.BUTTON_RESTART) != null) {
            HttpSession session = req.getSession(false);
            long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);
            gameService.restartGame(gameId);
            redirectAddress = UrlHelper.QUESTION;
        } else if (req.getParameter(Key.BUTTON_QUESTS) != null) {
            redirectAddress = UrlHelper.QUESTS;
            req.getSession().removeAttribute(Key.GAME);
        }
        resp.sendRedirect(redirectAddress);
    }
}
