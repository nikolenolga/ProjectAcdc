package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.User;
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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {UrlHelper.EDIT_USER})
public class EditUserServlet extends HttpServlet {
    private UserService userService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.EDIT_USER);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        long userId = RequestHelper.getLongValue(session, Key.USER_ID);
        String name = req.getParameter(Key.NAME);
        String password = req.getParameter(Key.PASSWORD);

        Optional<User> optionalUser = userService.get(userId);
        if(optionalUser.isPresent() && !StringUtils.isAnyBlank(name, password)) {
            User user = optionalUser.get();
            user.setName(name);
            user.setPassword(password);
            userService.update(user);

            session.setAttribute(Key.USER, user);
            resp.sendRedirect(UrlHelper.EDIT_USER);
        } else {
            resp.sendRedirect(UrlHelper.EDIT_USER  + "?" + Key.ALERT + "=" + Key.CANT_UPDATE);
        }
    }
}
