package com.javarush.khmelov.cmd;

import com.javarush.khmelov.entity.Game;
import com.javarush.khmelov.entity.Question;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.service.GameService;
import com.javarush.khmelov.service.QuestionService;
import com.javarush.khmelov.util.Go;
import com.javarush.khmelov.util.Key;
import com.javarush.khmelov.util.Parser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class Play implements Command {

    private final GameService gameService;
    private final QuestionService questionService;

    public Play(GameService gameService, QuestionService questionService) {
        this.gameService = gameService;
        this.questionService = questionService;
    }

    @Override
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        Long questId = Long.parseLong(request.getParameter(Key.QUEST_ID));
        Optional<User> user = Parser.getUser(request.getSession());
        if (user.isPresent()) {
            Long userId = user.get().getId();
            Optional<com.javarush.khmelov.entity.Game> game = gameService.getGame(questId, userId);
            if (game.isPresent()) {
                showOneQuestion(request, game.get());
                Long id = game.get().getId();
                return getJspPage() + "?id=" + id;
            } else {
                createError(request,"Нет незавершенной игры");
                return Go.HOME;
            }
        } else {
            createError(request,"Сначала нужно войти в аккаунт");
            return Go.LOGIN;
        }
    }

    @Override
    public String doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long gameId = Parser.getId(request);
        Long answerId = Parser.getId(request, Key.ANSWER);
        Optional<com.javarush.khmelov.entity.Game> game = gameService.checkAnswer(gameId, answerId);
        if (game.isPresent()) {
            com.javarush.khmelov.entity.Game gameDto = game.get();
            if (answerId == 0) {
                createError(request, "Нужно выбрать какой-то ответ");
            }
            showOneQuestion(request, gameDto);
            return Go.PLAY+"?id=" + gameId;
        } else {
            createError(request, "Нет такой игры");
            return Go.HOME;
        }
    }

    private void showOneQuestion(HttpServletRequest request, Game game) {
        request.setAttribute(Key.GAME, game);
        Optional<Question> question = questionService.get(game.getCurrentQuestionId());
        request.setAttribute(Key.QUESTION, question.orElseThrow());
    }

    private static void createError(HttpServletRequest request, String errorMessage) {
        request.getSession().setAttribute(Key.ERROR_MESSAGE, errorMessage);
    }
}
