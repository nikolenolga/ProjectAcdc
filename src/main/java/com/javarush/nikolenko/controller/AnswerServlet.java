package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.AnswerService;
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
    private AnswerService answerService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
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
        req.setAttribute("isFinal", answer.isFinal());
        req.setAttribute("isWin", answer.isWin());

        if (answer.hasNextQuestionLogic()) {
            resp.sendRedirect("question?currentQuestionId=" + answer.getNextQuestionId());
        } else {
            req.setAttribute("button", answer.isFinal() ? "Начать заново" : "Дальше");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("WEB-INF/answer.jsp");
            requestDispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, "answerId");
        Optional<Answer> optionalAnswer = answerService.get(answerId);
        Answer answer = optionalAnswer.get();
        long redirectId = answer.getNextQuestionId();
        if(answer.isFinal()) {
            HttpSession httpSession = req.getSession(false);
            Quest quest = RequestHelper.extractAttribute(httpSession, "quest", Quest.class);
            redirectId = quest.getCurrentQuestionId();
        }
        resp.sendRedirect("question?currentQuestionId=" + redirectId);
    }
}
