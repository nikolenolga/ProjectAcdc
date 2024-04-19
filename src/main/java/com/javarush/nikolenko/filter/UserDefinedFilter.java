package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.config.Configuration;
import com.javarush.nikolenko.config.ServiceLocator;
import com.javarush.nikolenko.utils.AppStaticComponents;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;

@WebFilter(urlPatterns = {UrlHelper.INDEX, UrlHelper.PLAY, UrlHelper.QUESTS,
        UrlHelper.QUESTION, UrlHelper.ANSWER, UrlHelper.LOGIN,
        UrlHelper.REGISTRATION})
public class UserDefinedFilter extends HttpFilter {

    @SneakyThrows
    @Override
    public void init(FilterConfig config) throws ServletException {
        ServiceLocator.getService(Configuration.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session.getAttribute(Key.USER) == null) {
            session.setAttribute(Key.USER, AppStaticComponents.ANONYMOUS);
            session.setAttribute(Key.USER_ID, AppStaticComponents.ANONYMOUS.getId());
            session.setAttribute(Key.IS_AUTHORIZED, false);
        }

        chain.doFilter(req, res);
    }

}
