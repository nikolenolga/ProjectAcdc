package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.QuestionTo;
import com.javarush.nikolenko.service.AnswerService;
import com.javarush.nikolenko.service.GameService;
import com.javarush.nikolenko.service.QuestionService;
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
import java.util.Collection;

@WebServlet(urlPatterns = {UrlHelper.QUESTION})
public class QuestionServlet extends HttpServlet {
    private GameService gameService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        gameService = NanoSpring.find(GameService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);

        QuestionTo questionTo = gameService.getCurrentQuestionWithAnswers(gameId);
        Collection<AnswerTo> answers = questionTo.getPossibleAnswers();

        req.setAttribute(Key.QUESTION, questionTo);
        req.setAttribute(Key.ANSWERS, answers);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(UrlHelper.getJspPath(UrlHelper.QUESTION));
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);

        String redirectPath = gameService.processAnswer(answerId, gameId)
                ? UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.ANSWER, Key.ANSWER_ID, answerId)
                : UrlHelper.QUESTION;
        resp.sendRedirect(redirectPath);
    }
}
