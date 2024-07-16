package com.javarush.nikolenko.filter;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.service.QuestService;
import com.javarush.nikolenko.utils.Key;
import com.javarush.nikolenko.utils.RequestHelper;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;

@WebFilter(urlPatterns = {UrlHelper.EDIT_QUEST})
public class EditQuestComponentsFilter extends HttpFilter {
    private QuestService questService;

    @SneakyThrows
    @Override
    public void init(FilterConfig config) throws ServletException {
        questService = NanoSpring.find(QuestService.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        boolean needToAddQuestion = req.getParameter(Key.BUTTON_ADD_QUESTION) != null;
        boolean needToAddAnswer = req.getParameter(Key.BUTTON_ADD_ANSWER) != null;
        boolean needToDeleteQuest = req.getParameter(Key.BUTTON_DELETE_QUEST) != null;
        boolean isPostMethod = req.getMethod().equalsIgnoreCase("post");

        if (isPostMethod && (needToAddAnswer || needToAddQuestion || needToDeleteQuest)) {
            long questId = RequestHelper.getLongValue(req, Key.QUEST_ID);

            if (needToDeleteQuest) {
                if (!questService.delete(questId)) {
                    throw new QuestException("Can't delete quest %s".formatted(questId));
                }
            }

            String redirectAdress = getRedirectAdress(req, questId, needToDeleteQuest, needToAddQuestion, needToAddAnswer);
            res.sendRedirect(redirectAdress);
        } else {
            chain.doFilter(req, res);
        }
    }

    private String getRedirectAdress(HttpServletRequest req, long questId,
                                     boolean needToDeleteQuest, boolean needToAddQuestion, boolean needToAddAnswer) {

        if (needToAddQuestion) {
            return UrlHelper.ONE_PARAM_TEMPLATE.formatted(
                    UrlHelper.ADD_QUESTION,
                    Key.QUEST_ID, questId);
        }

        if (needToAddAnswer) {
            long questionId = RequestHelper.getLongValue(req, Key.QUESTION_ID);
            return UrlHelper.TWO_PARAM_TEMPLATE.formatted(
                    UrlHelper.ADD_ANSWER,
                    Key.QUEST_ID, questId,
                    Key.QUESTION_ID, questionId);
        }

        return UrlHelper.USER_QUESTS;
    }
}
