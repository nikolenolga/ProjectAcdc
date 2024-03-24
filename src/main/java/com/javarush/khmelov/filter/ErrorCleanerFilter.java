package com.javarush.khmelov.filter;

import com.javarush.khmelov.util.Key;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class ErrorCleanerFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);

        if (req.getMethod().equalsIgnoreCase("get")) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.removeAttribute(Key.ERROR_MESSAGE);
            }
        }
    }
}
