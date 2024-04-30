package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Question;
import com.javarush.nikolenko.service.QuestModifyService;
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

@WebServlet(urlPatterns = {UrlHelper.ADD_QUESTION})
public class AddQuestionServlet extends HttpServlet {
    QuestModifyService questModifyService;
    private QuestionService questionService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questionService = ServiceLocator.getService(QuestionService.class);
        questModifyService = ServiceLocator.getService(QuestModifyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = Long.parseLong(req.getParameter(Key.QUEST_ID));
        req.setAttribute(Key.QUEST_ID, questId);
        String jspPath = UrlHelper.getJspPath(UrlHelper.ADD_QUESTION);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        String redirectPath = UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, questId);

        if (req.getParameter(Key.BUTTON_ADD_QUESTION) != null) {
            String questionMessage = req.getParameter(Key.QUESTION_MESSAGE);
            Question question = Question.builder()
                    .questionMessage(questionMessage)
                    .build();

            questionService.create(question);
            questModifyService.addQuestion(questId, question.getId());
        }

        resp.sendRedirect(redirectPath);
    }
}
