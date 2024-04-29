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
    public static final String QUEST_TEXT_EDITOR = "/quest-text-editor";
    public static final String USER_QUESTS = "/user-quests";
    public static final String IMAGES = "/images/*";
    public static final String UPLOAD_IMAGE = "/uploadImage";
    public static final String RANDOM_ERROR = "/randomError";
    public static final String ERROR_HANDLER = "/errorHandler";
    public static final String ERROR = "/error";


    public static final String QUEST_DIRECTORY = "quests";
    public static final String IMAGE_DIRECTORY = "images";
    public static final String JSP_DIRECTORY = "WEB-INF/views";
    public static final String ONE_PARAM_TEMPLATE = "%s?%s=%s";
    public static final String TWO_PARAM_TEMPLATE = "%s?%s=%s&%s=%s";

    public static String getJspPath(String path) {
        return JSP_DIRECTORY + path + ".jsp";
    }
}
