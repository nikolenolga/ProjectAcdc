package com.javarush.khmelov.cmd;

import com.javarush.khmelov.entity.Game;
import com.javarush.khmelov.entity.Question;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.service.GameService;
import com.javarush.khmelov.service.QuestionService;
import com.javarush.khmelov.util.Go;
import com.javarush.khmelov.util.Key;
import com.javarush.khmelov.util.RequestHelper;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@SuppressWarnings("unused")
public class PlayGame implements Command {

    private final GameService gameService;
    private final QuestionService questionService;

    public PlayGame(GameService gameService, QuestionService questionService) {
        this.gameService = gameService;
        this.questionService = questionService;
    }

    @Override
    public String doGet(HttpServletRequest request) {
        Long questId = Long.parseLong(request.getParameter(Key.QUEST_ID));
        Optional<User> user = RequestHelper.getUser(request.getSession());
        if (user.isPresent()) {
            Long userId = user.get().getId();
            Optional<Game> game = gameService.getGame(questId, userId);
            if (game.isPresent()) {
                showOneQuestion(request, game.get());
                return getJspPage();
            } else {
                RequestHelper.setError(request, "Нет незавершенной игры");
                return Go.HOME;
            }
        } else {
            RequestHelper.setError(request, "Сначала нужно войти в аккаунт");
            return Go.LOGIN;
        }
    }

    @Override
    public String doPost(HttpServletRequest request) {
        Long gameId = RequestHelper.getId(request);
        Long answerId = RequestHelper.getId(request, Key.ANSWER);
        Optional<Game> gameOptional = gameService.processOneStep(gameId, answerId);
        if (gameOptional.isPresent()) {
            if (answerId == 0 && request.getParameter("new-game") == null) {
                RequestHelper.setError(request, "Нужно выбрать какой-то ответ");
            }
            Game game = gameOptional.get();
            return "%s?questId=%d&id=%d".formatted(Go.PLAY_GAME, game.getQuestId(), game.getId());
        } else {
            RequestHelper.setError(request, "Нет такой игры");
            return Go.HOME;
        }
    }

    private void showOneQuestion(HttpServletRequest request, Game game) {
        request.setAttribute(Key.GAME, game);
        Optional<Question> question = questionService.get(game.getCurrentQuestionId());
        request.setAttribute(Key.QUESTION, question.orElseThrow());
    }

}
