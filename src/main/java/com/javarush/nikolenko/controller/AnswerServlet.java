package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.AnswerTo;
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

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        gameService = NanoSpring.find(GameService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);

        AnswerTo answerTo = gameService.checkAnswer(answerId, gameId);
        req.setAttribute(Key.ANSWER, answerTo);

        String jspPath = UrlHelper.getJspPath(UrlHelper.ANSWER);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String redirectAddress = UrlHelper.INDEX;
        HttpSession session = req.getSession(false);
        long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);

        if (req.getParameter(Key.BUTTON_NEXT) != null) {
            redirectAddress = UrlHelper.QUESTION;
        } else if (req.getParameter(Key.BUTTON_RESTART) != null) {
            gameService.restartGame(gameId);
            redirectAddress = UrlHelper.QUESTION;
        } else if (req.getParameter(Key.BUTTON_QUESTS) != null) {
            redirectAddress = UrlHelper.QUESTS;
            session.removeAttribute(Key.GAME);
            session.removeAttribute(Key.GAME_ID);
        }

        resp.sendRedirect(redirectAddress);
    }
}
