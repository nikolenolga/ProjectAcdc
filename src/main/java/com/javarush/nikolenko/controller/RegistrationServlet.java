package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.GameState;
import com.javarush.nikolenko.entity.Role;
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

@WebServlet(urlPatterns = UrlHelper.REGISTRATION)
public class RegistrationServlet extends HttpServlet {
    private UserService userService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = NanoSpring.find(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPath = UrlHelper.getJspPath(UrlHelper.REGISTRATION);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(jspPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = req.getParameter(Key.LOGIN);

        if (!userService.userExist(login)) {
            User user = User.builder()
                    .name(req.getParameter(Key.NAME))
                    .login(login)
                    .password(req.getParameter(Key.PASSWORD))
                    .role(Role.THE_USER)
                    .build();

            Optional<User> optionalUser = userService.create(user);

            session.setAttribute(Key.USER, user);
            session.setAttribute(Key.USER_ID, user.getId());
            session.setAttribute(Key.IS_AUTHORIZED, optionalUser.isPresent());

            resp.sendRedirect(UrlHelper.EDIT_USER);
        } else {
            resp.sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.REGISTRATION,
                    Key.ALERT, Key.USER_EXIST));
        }
    }
}
