package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.service.ImageService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@MultipartConfig
@WebServlet(urlPatterns = {UrlHelper.EDIT_USER})
public class EditUserServlet extends HttpServlet {
    private UserService userService;
    private ImageService imageService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = NanoSpring.find(UserService.class);
        imageService = NanoSpring.find(ImageService.class);
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
        UserTo user = (UserTo) session.getAttribute(Key.USER);
        String name = req.getParameter(Key.NAME);
        String password = req.getParameter(Key.PASSWORD);
        String redirectPath = UrlHelper.EDIT_USER;

        if (req.getParameter(Key.BUTTON_USER_IMG_LOAD) != null) {
            imageService.uploadImage(req, user.getImage());
        } else if (req.getParameter(Key.BUTTON_SUBMIT) != null && !StringUtils.isAnyBlank(password)) {
            userService.update(user, name, password);
            session.setAttribute(Key.USER, user);
        } else {
            redirectPath = UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.EDIT_USER,
                    Key.ALERT, Key.CANT_UPDATE);
        }

        resp.sendRedirect(redirectPath);
    }
}
