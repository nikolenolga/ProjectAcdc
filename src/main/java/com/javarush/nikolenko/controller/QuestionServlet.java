package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestionService;
import com.javarush.nikolenko.utils.RequestHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@WebServlet(urlPatterns = "/question")
public class QuestionServlet extends HttpServlet {
    private QuestionService questionService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questionService = ServiceLocator.getService(QuestionService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long currentQuestionId = RequestHelper.getLongValue(req, "currentQuestionId");
        Optional<Question> optionalQuestion = questionService.get(currentQuestionId);
        if (optionalQuestion.isEmpty()) {
            throw new QuestException("No question found with id %d".formatted(currentQuestionId));
        }

        Question question = optionalQuestion.get();
        Collection<Answer> answers = questionService.getAnswersByQuestionId(currentQuestionId);
        if(answers.isEmpty()){
            throw new QuestException("No answers found for question with id %d".formatted(currentQuestionId));
        }

        req.setAttribute("question", question);
        req.setAttribute("answers", answers);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("WEB-INF/question.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long answerId = RequestHelper.getLongValue(req, "answerId");
        if(answerId == 0){
            throw new QuestException("Peeked answer not found.");
        }
        resp.sendRedirect("answer?answerId=" + answerId);
    }
}
