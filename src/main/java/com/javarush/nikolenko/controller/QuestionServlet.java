package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.exception.QuestException;
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
import java.util.Optional;

@WebServlet(urlPatterns = {UrlHelper.QUESTION})
public class QuestionServlet extends HttpServlet {
    private GameService gameService;
    private QuestionService questionService;
    private AnswerService answerService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        gameService = ServiceLocator.getService(GameService.class);
        questionService = ServiceLocator.getService(QuestionService.class);
        answerService = ServiceLocator.getService(AnswerService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);
        long currentQuestionId = gameService.getCurrentQuestionId(gameId);
        Question question = questionService.get(currentQuestionId).get();
        Collection<Answer> answers = questionService.getAnswersByQuestionId(currentQuestionId);

        req.setAttribute(Key.QUESTION, question);
        req.setAttribute(Key.ANSWERS, answers);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(UrlHelper.getJspPath(UrlHelper.QUESTION));
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, Key.ANSWER_ID);
        HttpSession session = req.getSession(false);
        long gameId = RequestHelper.getLongValue(session, Key.GAME_ID);
        long nextQuestionId = answerService.getNextQuestionId(answerId);
        gameService.setNextQuestion(gameId, nextQuestionId);

        String redirectPath = answerService.hasFinalMessage(answerId)
                ? UrlHelper.getUrlWithParameter(UrlHelper.ANSWER, Key.ANSWER_ID) + answerId
                : UrlHelper.QUESTION;
        resp.sendRedirect(redirectPath);
    }
}
