package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.Key;
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

@WebServlet(urlPatterns = {UrlHelper.LOGIN})
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.LOGIN);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = req.getParameter(Key.LOGIN);
        String password = req.getParameter(Key.PASSWORD);
        Optional<User> optionalUser = userService.getUser(login, password);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            session.setAttribute(Key.USER, user);
            session.setAttribute(Key.USER_ID, user.getId());
            session.setAttribute(Key.IS_AUTHORIZED, true);
            resp.sendRedirect(UrlHelper.EDIT_USER);
        } else {
            resp.sendRedirect(UrlHelper.LOGIN + "?" + Key.ALERT + "=" + Key.WRONG_USER_DATA);
        }
    }

}
