package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = {UrlHelper.EDIT_USER})
public class LogOutFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getParameter(Key.BUTTON_EXIT) != null) {
            HttpSession session = req.getSession(false);
            session.removeAttribute(Key.USER);
            session.removeAttribute(Key.USER_ID);
            session.setAttribute(Key.IS_AUTHORIZED, false);
            res.sendRedirect(UrlHelper.LOGIN);
            log.info("User {} logged out", (UserTo) session.getAttribute(Key.USER));
        } else {
            chain.doFilter(req, res);
        }
    }
}
