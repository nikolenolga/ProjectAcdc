package com.javarush.khmelov.cmd;

import com.javarush.khmelov.service.StatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.javarush.khmelov.util.Key.*;

@SuppressWarnings("unused")
public class Statistics implements Command {

    private final StatService statService;

    public Statistics(StatService statService) {
        this.statService = statService;
    }

    @Override
    public String doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute(LIST_USER_STATISTICS, statService.getUserStatistics());
        req.setAttribute(TOTAL_USER_STATISTICS, statService.getTotalUserStatistics());
        return getJspPage();
    }
}
