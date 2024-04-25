package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Answer;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.service.AnswerService;
import com.javarush.nikolenko.service.QuestModifyService;
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
    private AnswerService answerService;
    private QuestModifyService questModifyService;


    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        answerService = ServiceLocator.getService(AnswerService.class);
        questModifyService = ServiceLocator.getService(QuestModifyService.class);

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

        if(req.getParameter(Key.BUTTON_ADD_ANSWER) != null) {
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            String answerMessage = req.getParameter(Key.ANSWER_MESSAGE);
            GameState gameState = GameState.valueOf(req.getParameter(Key.GAMESTATE));
            long nextQuestionId = RequestHelper.getLongValue(req, Key.NEXT_QUESTION_ID);
            String finalMessage = req.getParameter(Key.FINAL_MESSAGE);

            Answer answer = Answer.builder()
                    .answerMessage(answerMessage)
                    .gameState(gameState)
                    .nextQuestionId(nextQuestionId)
                    .finalMessage(finalMessage)
                    .build();

            answerService.create(answer);
            questModifyService.addAnswer(questId, questionId, answer.getId());
        }

        resp.sendRedirect(redirectPath);
    }
}
