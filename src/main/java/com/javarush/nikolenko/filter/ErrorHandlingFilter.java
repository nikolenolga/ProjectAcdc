package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = {UrlHelper.INDEX, UrlHelper.PLAY, UrlHelper.QUESTS,
        UrlHelper.QUESTION, UrlHelper.ANSWER, UrlHelper.LOGIN,
        UrlHelper.REGISTRATION, UrlHelper.EDIT_USER, UrlHelper.EDIT_QUEST,
        UrlHelper.ADD_QUESTION, UrlHelper.ADD_ANSWER, UrlHelper.CREATE_QUEST,
        UrlHelper.QUEST_TEXT_EDITOR, UrlHelper.USER_QUESTS, UrlHelper.IMAGES_X,
        UrlHelper.UPLOAD_IMAGE, UrlHelper.RANDOM_ERROR, UrlHelper.ERROR_HANDLER})
public class ErrorHandlingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (Throwable t) {
            if (t instanceof ServletException) {
                int errorCode = res.getStatus();
                req.setAttribute(Key.ERROR_CODE, errorCode);
                log.error("Error occurred while processing request: {}", t.toString());
            }
            //req.setAttribute(Key.DESCRIPTION, t.toString());

            String errorMessage = (String) req.getAttribute(Key.ALERT);

            if(errorMessage != null) {
                req.setAttribute(Key.ERROR_MESSAGE, errorMessage);
            }

            String jspPath = UrlHelper.getJspPath(UrlHelper.ERROR);
            req.getRequestDispatcher(jspPath).forward(req, res);
        }
    }
}



