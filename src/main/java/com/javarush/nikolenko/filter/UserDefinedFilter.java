package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.service.UserService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.LoggerConstants;
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
@WebFilter(urlPatterns = {UrlHelper.INDEX, UrlHelper.PLAY, UrlHelper.QUESTS,
        UrlHelper.QUESTION, UrlHelper.ANSWER, UrlHelper.LOGIN,
        UrlHelper.REGISTRATION})
public class UserDefinedFilter extends HttpFilter {
    private UserService userService;

    @SneakyThrows
    @Override
    public void init(FilterConfig config) throws ServletException {
        userService = NanoSpring.find(UserService.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session.getAttribute(Key.USER) == null) {

            UserTo anonymous = userService.createAnonymousUser().orElseThrow();
            session.setAttribute(Key.USER, anonymous);
            session.setAttribute(Key.USER_ID, anonymous.getId());
            session.setAttribute(Key.IS_AUTHORIZED, false);
            log.info(LoggerConstants.DEFAULT_USER_IS_SET, anonymous.getName());
        }

        chain.doFilter(req, res);
    }

}
