package com.javarush.khmelov.cmd;

import com.javarush.khmelov.entity.Question;
import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.service.ImageService;
import com.javarush.khmelov.service.QuestService;
import com.javarush.khmelov.service.QuestionService;
import com.javarush.khmelov.util.Go;
import com.javarush.khmelov.util.Key;
import com.javarush.khmelov.util.RequestHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;

import static com.javarush.khmelov.util.Key.QUEST;


@SuppressWarnings("unused")
public class Quest implements Command {

    private final QuestService questService;
    private final QuestionService questionService;
    private final ImageService imageService;

    public Quest(QuestService questService, QuestionService questionService, ImageService imageService) {
        this.questService = questService;
        this.questionService = questionService;
        this.imageService = imageService;
    }

    @Override
    public String doGet(HttpServletRequest req) {
        long id = RequestHelper.getId(req);
        Optional<com.javarush.khmelov.entity.Quest> quest = questService.get(id);
        req.setAttribute(QUEST, quest.orElseThrow());
        return getJspPage();
    }

    @Override
    public String doPost(HttpServletRequest req) throws IOException, ServletException {
        Optional<User> editor = RequestHelper.getUser(req.getSession());
        if (editor.isPresent() && editor.get().getRole() == Role.ADMIN) {
            Long id = RequestHelper.getId(req);
            Long questionId = RequestHelper.getId(req, Key.QUESTION_ID);
            String text = req.getParameter(Key.TEXT);
            Optional<Question> question = questionService.update(questionId, text);
            if (question.isPresent()) {
                imageService.uploadImage(req, question.get().getImage());
            }
            return "%s?id=%d#bookmark%d".formatted(Go.QUEST, id, questionId);
        } else {
            return Go.QUEST; //TODO добавить ошибку, что "Недостаточно прав для редактирования";
        }
    }
}
