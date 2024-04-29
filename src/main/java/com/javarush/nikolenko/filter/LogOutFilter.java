package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {UrlHelper.EDIT_USER})
public class LogOutFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(req.getParameter(Key.BUTTON_EXIT) != null) {
            HttpSession session = req.getSession(false);
            session.removeAttribute(Key.USER);
            session.removeAttribute(Key.USER_ID);
            session.setAttribute(Key.IS_AUTHORIZED, false);
            res.sendRedirect(UrlHelper.LOGIN);
        } else {
            chain.doFilter(req, res);
        }
    }
}
