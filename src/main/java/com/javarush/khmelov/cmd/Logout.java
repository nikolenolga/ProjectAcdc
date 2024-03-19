package com.javarush.khmelov.cmd;

import com.javarush.khmelov.util.Go;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@SuppressWarnings("unused")
public class Logout implements Command {
    @Override
    public String doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        return Go.LOGIN;
    }
}
