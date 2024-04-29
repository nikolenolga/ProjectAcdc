package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.service.ImageService;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.*;
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
        userService = ServiceLocator.getService(UserService.class);
        imageService = ServiceLocator.getService(ImageService.class);
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
        User user = userService.get(userId).get();
        String redirectPath = UrlHelper.EDIT_USER;

        if(req.getParameter(Key.BUTTON_USER_IMG_LOAD) != null){
            imageService.uploadImage(req, user.getImage());
        } else if(req.getParameter(Key.BUTTON_SUBMIT) != null && !StringUtils.isAnyBlank(name, password)) {
            user.setName(name);
            user.setPassword(password);
            userService.update(user);

            session.setAttribute(Key.USER, user);
        } else {
            redirectPath = UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.EDIT_USER,
                    Key.ALERT, Key.CANT_UPDATE);
        }

        resp.sendRedirect(redirectPath);
    }
}
