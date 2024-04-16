package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.service.QuestService;
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

@WebServlet(urlPatterns = {"/quests"})
public class QuestsServlet extends HttpServlet {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) {
        questService = ServiceLocator.getService(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Quest> quests = questService.getAll();
        req.setAttribute("quests", quests);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("WEB-INF/views/quests.jsp");
        requestDispatcher.forward(req, resp);
    }
    
}
