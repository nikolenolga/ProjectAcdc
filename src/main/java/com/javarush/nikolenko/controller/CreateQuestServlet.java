package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.UserTo;
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
import java.util.Optional;

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
        long authorId = RequestHelper.getLongValue(req.getSession(), Key.USER_ID);
        String description = req.getParameter(Key.DESCRIPTION);

        Optional<QuestTo> optionalQuest = questService.createQuest(authorId, name, description);

        String redirectAdress = optionalQuest.isPresent()
                ? UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.EDIT_QUEST, Key.QUEST_ID, optionalQuest.get().getId())
                : UrlHelper.CREATE_QUEST;

        resp.sendRedirect(redirectAdress);
    }
}
