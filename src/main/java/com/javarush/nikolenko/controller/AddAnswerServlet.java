package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.AnswerTo;
import com.javarush.nikolenko.dto.GameState;
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
import lombok.SneakyThrows;

import java.io.IOException;

@WebServlet(urlPatterns = {UrlHelper.ADD_ANSWER})
public class AddAnswerServlet extends HttpServlet {
    private QuestionService questionService;


    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questionService = NanoSpring.find(QuestionService.class);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.ADD_ANSWER);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        String redirectPath = UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, questId
        );

        if (req.getParameter(Key.BUTTON_ADD_ANSWER) != null) {
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            String answerMessage = req.getParameter(Key.ANSWER_MESSAGE);
            GameState gameState = GameState.valueOf(req.getParameter(Key.GAMESTATE));
            long nextQuestionId = RequestHelper.getLongValue(req, Key.NEXT_QUESTION_ID);
            String finalMessage = req.getParameter(Key.FINAL_MESSAGE);

            AnswerTo answer = AnswerTo.builder()
                    .answerMessage(answerMessage)
                    .gameState(gameState)
                    .finalMessage(finalMessage)
                    .build();

            questionService.addNewAnswerToCreatedQuestion(questionId, answer, nextQuestionId);
        }

        resp.sendRedirect(redirectPath);
    }
}
