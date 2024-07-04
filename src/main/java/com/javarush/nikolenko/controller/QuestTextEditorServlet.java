package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.service.QuestEditService;
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
    private QuestEditService questEditService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        questEditService = NanoSpring.find(QuestEditService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.QUEST_TEXT_EDITOR);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String text = req.getParameter(Key.TEXT);
        session.setAttribute(Key.TEXT, text);
        UserTo user = (UserTo) session.getAttribute(Key.USER);
        String redirectPath = UrlHelper.QUEST_TEXT_EDITOR;

        if (req.getParameter(Key.BUTTON_ADD_QUEST) != null) {
            Optional<QuestTo> optionalQuest = questEditService.parseQuest(user, text);
            redirectPath = optionalQuest.isEmpty()
                    ? UrlHelper.TWO_PARAM_TEMPLATE.formatted(UrlHelper.QUEST_TEXT_EDITOR,
                        Key.HAS_ALERTS, true,
                        Key.ALERT, Key.PARSE_EXCEPTION)
                    : UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.EDIT_QUEST,
                        Key.QUEST_ID, optionalQuest.get().getId());
        } else if (req.getParameter(Key.BUTTON_RESET) != null) {
            session.removeAttribute(Key.TEXT);
        }

        resp.sendRedirect(redirectPath);
    }
}
