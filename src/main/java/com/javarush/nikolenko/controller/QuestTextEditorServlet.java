package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.utils.Key;
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

@WebServlet(urlPatterns = {UrlHelper.QUEST_TEXT_EDITOR})
public class QuestTextEditorServlet extends HttpServlet {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = ServiceLocator.getService(QuestService.class);
        String rules = questService.loadRules();
        config.getServletContext().setAttribute(Key.RULES, rules);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.QUEST_TEXT_EDITOR);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("text") != null) {}
    }
}
