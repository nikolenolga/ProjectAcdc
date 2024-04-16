package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.Configuration;
import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.AppStaticComponents;
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

@WebServlet(urlPatterns = {""})
public class InitServlet extends HttpServlet {
    private Configuration configuration;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        configuration = ServiceLocator.getService(Configuration.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session.getAttribute("user") == null) {
            session.setAttribute("user", AppStaticComponents.ANONYMOUS);
            session.setAttribute("userId", AppStaticComponents.ANONYMOUS.getId());
            session.setAttribute("authorized", false);
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("quests");
        requestDispatcher.forward(req, resp);
    }
}
