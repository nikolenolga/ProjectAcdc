package com.javarush.khmelov.cmd;

import com.javarush.khmelov.service.QuestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.javarush.khmelov.util.Key.QUESTS;

@SuppressWarnings("unused")
public class Home implements Command {

    private final QuestService questService;

    public Home(QuestService questService) {
        this.questService = questService;
    }

    @Override
    public String doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute(QUESTS, questService.getAll());
        return getJspPage();
    }
}
