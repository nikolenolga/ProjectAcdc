package com.javarush.nikolenko.utils;

public final class UrlHelper {
    public static final String INDEX = "";
    public static final String PLAY = "/play";
    public static final String QUESTS = "/quests";
    public static final String QUESTION = "/question";
    public static final String ANSWER = "/answer";
    public static final String LOGIN = "/login";
    public static final String REGISTRATION = "/registration";
    public static final String EDIT_USER = "/edit-user";
    public static final String EDIT_QUEST = "/edit-quest";
    public static final String ADD_QUESTION = "/add-question";
    public static final String ADD_ANSWER = "/add-answer";
    public static final String CREATE_QUEST = "/create-quest";
    public static final String LOAD_QUEST = "/load-quest";
    public static final String QUEST_TEXT_EDITOR = "/quest-text-editor";
    public static final String USER_QUESTS = "/user-quests";
    public static final String USER_STATISTIC = "/user-statistic";

    public static final String QUEST_DIRECTORY = "WEB-INF/quests";
    public static final String IMAGE_DIRECTORY = "WEB-INF/images";
    public static final String JSP_DIRECTORY = "WEB-INF/views";
    public static final String ONE_PARAM_TEMPLATE = "%s?%s=%s";
    public static final String TWO_PARAM_TEMPLATE = "%s?%s=%s&%s=%s";
    public static final String RULES_FILE = "WEB-INF/static/rules.txt";

    public static String getJspPath(String path) {
        return JSP_DIRECTORY + path + ".jsp";
    }

    public static String getUrlWithParameter(String url, String parameter) {
        return url + "?" + parameter + "=";
    }
}
