package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.service.GameService;
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
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;

@WebServlet(urlPatterns = {UrlHelper.PLAY})
public class PlayServlet extends HttpServlet {
    private QuestService questService;
    private GameService gameService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = NanoSpring.find(QuestService.class);
        gameService = NanoSpring.find(GameService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);
        Quest quest = questService.get(questId).orElseThrow();
        req.setAttribute(Key.QUEST, quest);
        req.setAttribute(Key.QUEST_ID, questId);

        String jspPath = UrlHelper.getJspPath(UrlHelper.PLAY);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        long userId = RequestHelper.getLongValue(session, Key.USER_ID);
        User player = (User) session.getAttribute(Key.USER);
        long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);

        Game game = gameService.initGame(player, questId);
        session.setAttribute(Key.GAME, game);
        session.setAttribute(Key.GAME_ID, game.getId());

        String redirectAddress = req.getParameter(Key.BUTTON_START) != null
                ? UrlHelper.QUESTION
                : UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.PLAY, Key.QUEST_ID, questId);
        resp.sendRedirect(redirectAddress);
    }
}
