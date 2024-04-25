package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {UrlHelper.LOGIN, UrlHelper.REGISTRATION, UrlHelper.EDIT_USER})
public class AlertsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //перенос сообщений из параметров запроса в атрибуты
        if(req.getMethod().equalsIgnoreCase("get") && req.getParameter(Key.ALERT) != null) {
            req.setAttribute(Key.HAS_ALERTS, true);
            String alert = req.getParameter(Key.ALERT);
            req.setAttribute(Key.ALERT, alert);
        }

        chain.doFilter(req, res);
    }
}
