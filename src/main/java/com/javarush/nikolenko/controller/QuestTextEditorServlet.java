package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.service.QuestModifyService;
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
import java.util.Optional;

@WebServlet(urlPatterns = {UrlHelper.QUEST_TEXT_EDITOR})
public class QuestTextEditorServlet extends HttpServlet {
    private QuestService questService;
    private QuestModifyService questModifyService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questService = ServiceLocator.getService(QuestService.class);
        questModifyService = ServiceLocator.getService(QuestModifyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jspPath = UrlHelper.getJspPath(UrlHelper.QUEST_TEXT_EDITOR);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectPath = UrlHelper.QUEST_TEXT_EDITOR;
        HttpSession session = req.getSession();
        String text = req.getParameter(Key.TEXT);
        session.setAttribute(Key.TEXT, text);
        long userId = RequestHelper.getLongValue(session, Key.USER_ID);

        if(req.getParameter(Key.BUTTON_ADD_QUEST) != null) {
            Optional<Quest> optionalQuest = questModifyService.parseQuest(userId, text);
            if(optionalQuest.isEmpty()) {
                redirectPath = UrlHelper.TWO_PARAM_TEMPLATE.formatted(UrlHelper.QUEST_TEXT_EDITOR,
                        Key.HAS_ALERTS, true,
                        Key.ALERT, Key.PARSE_EXCEPTION);
            } else {
                long questId = optionalQuest.get().getId();
                redirectPath = UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.EDIT_QUEST,
                        Key.QUEST_ID, questId);
            }
        }
        if(req.getParameter(Key.BUTTON_EXPORT) != null) {

        }
        if(req.getParameter(Key.BUTTON_RESET) != null) {
            session.removeAttribute(Key.TEXT);
        }
        if(req.getParameter(Key.BUTTON_LOAD) != null) {

        }
        resp.sendRedirect(redirectPath);
    }
}
