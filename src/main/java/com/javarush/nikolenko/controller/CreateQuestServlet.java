package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.service.QuestService;
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

@WebServlet(urlPatterns = {UrlHelper.CREATE_QUEST})
public class CreateQuestServlet extends HttpServlet {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = NanoSpring.find(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.CREATE_QUEST);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(Key.NAME);
        long userAuthorId = RequestHelper.getLongValue(req.getSession(), Key.USER_ID);
        User author = (User) req.getSession().getAttribute(Key.USER);
        String description = req.getParameter(Key.DESCRIPTION);

        Quest quest = Quest.builder()
                .name(name)
                .author(author)
                .description(description)
                .firstQuestionId(0L)
                .build();
        questService.create(quest);

        resp.sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.EDIT_QUEST,
                Key.QUEST_ID, quest.getId()));
    }
}
