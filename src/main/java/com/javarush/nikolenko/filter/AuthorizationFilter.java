package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.config.Configurator;
import com.javarush.nikolenko.config.NanoSpring;
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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = {UrlHelper.EDIT_USER, UrlHelper.EDIT_QUEST, UrlHelper.USER_QUESTS, UrlHelper.QUEST_TEXT_EDITOR})
public class AuthorizationFilter extends HttpFilter {
    @SneakyThrows
    @Override
    public void init(FilterConfig config) throws ServletException {
        NanoSpring.find(Configurator.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Object authorized = session.getAttribute(Key.IS_AUTHORIZED);

        if (authorized != null && (Boolean) authorized) {
            chain.doFilter(req, res);
        } else {
            res.sendRedirect(UrlHelper.ONE_PARAM_TEMPLATE.formatted(UrlHelper.LOGIN,
                    Key.ALERT, Key.NEED_TO_LOGIN));
            log.info("User is not authorized, request {} rejected", req.getRequestURI());
        }
    }
}
