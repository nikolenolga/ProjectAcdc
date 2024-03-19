package com.javarush.khmelov.controller;

import com.javarush.khmelov.cmd.Command;
import com.javarush.khmelov.config.Config;
import com.javarush.khmelov.config.Winter;
import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.util.Go;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MultipartConfig(fileSizeThreshold = 1 << 20)
@WebServlet({
        Go.INDEX, Go.HOME,
        Go.SIGNUP, Go.LOGIN, Go.LOGOUT,
        Go.LIST_USER, Go.PROFILE, Go.EDIT_USER,
        Go.CREATE_QUEST,
})
public class FrontController extends HttpServlet {


    private HttpResolver httpResolver;

    @Override
    public void init(ServletConfig config) {
        httpResolver = Winter.find(HttpResolver.class);
        Winter.find(Config.class).fillEmptyRepository();
        config.getServletContext().setAttribute("roles", Role.values());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        Matcher matcher = Pattern.compile("[a-z-]+").matcher(uri);
        String cmdName = "home";
        if (matcher.find()) {
            cmdName = matcher.group();
        }
        Command command = httpResolver.resolve(cmdName);
        if (req.getMethod().equalsIgnoreCase("post")) {
            String redirect = command.doPost(req, resp);
            resp.sendRedirect(redirect);
        } else if (req.getMethod().equalsIgnoreCase("get")) {
            String view = command.doGet(req, resp);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(view);
            requestDispatcher.forward(req, resp);
        } else {
            throw new UnsupportedOperationException(req.getMethod());
        }
    }
}
