package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.Configurator;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.Quest;
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
import java.util.Collection;

@WebServlet(urlPatterns = {UrlHelper.INDEX, UrlHelper.QUESTS})
public class QuestsServlet extends HttpServlet {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) {
        Configurator configurator = NanoSpring.find(Configurator.class);
        questService = NanoSpring.find(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Quest> quests = questService.getAll();
        req.setAttribute(Key.QUESTS, quests);
        String jspPath = UrlHelper.getJspPath(UrlHelper.QUESTS);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }
}
