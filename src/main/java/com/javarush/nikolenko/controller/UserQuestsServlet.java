package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.service.UserService;
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
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Collection;

@WebServlet(urlPatterns = {UrlHelper.USER_QUESTS})
public class UserQuestsServlet extends HttpServlet {
    private UserService userService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = NanoSpring.find(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        long userId = RequestHelper.getLongValue(session, Key.USER_ID);
        Collection<QuestTo> quests = userService.getUserQuests(userId);
        req.setAttribute(Key.QUESTS, quests);

        String jspPath = UrlHelper.getJspPath(UrlHelper.USER_QUESTS);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }
}
