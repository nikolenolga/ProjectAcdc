package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.config.Configurator;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
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
@WebFilter(urlPatterns = {UrlHelper.INDEX, UrlHelper.PLAY, UrlHelper.QUESTS,
        UrlHelper.QUESTION, UrlHelper.ANSWER, UrlHelper.LOGIN,
        UrlHelper.REGISTRATION})
public class UserDefinedFilter extends HttpFilter {
    private User anonymous;

    @SneakyThrows
    @Override
    public void init(FilterConfig config) throws ServletException {
        NanoSpring.find(Configurator.class);
        anonymous = User.builder()
                .name("Anonymous")
                .login("anonymous")
                .password("anonymous-anonymous")
                .role(Role.GUEST)
                .build();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session.getAttribute(Key.USER) == null) {
            session.setAttribute(Key.USER, anonymous);
            session.setAttribute(Key.USER_ID, anonymous.getId());
            session.setAttribute(Key.IS_AUTHORIZED, false);
            log.info("Default [{}] user is set", anonymous.getName());
        }

        chain.doFilter(req, res);
    }

}
