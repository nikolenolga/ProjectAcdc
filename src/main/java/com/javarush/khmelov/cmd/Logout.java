package com.javarush.khmelov.cmd;

import com.javarush.khmelov.util.Go;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("unused")
public class Logout implements Command {
    @Override
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        return Go.LOGIN;
    }
}
