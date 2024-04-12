package com.javarush.nikolenko.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quest extends AbstractComponent {
    private String name;
    private final List<Question> questions = new ArrayList<>();
    private long currentQuestionId;
    private String description;
    private GameState gameState;

    public Quest() {};

    public Quest(long id, String name, long currentQuestionId, String description) {
        super(id);
        this.name = name;
        this.currentQuestionId = currentQuestionId;
        this.description = description;
        gameState = GameState.GAME;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public String getName() {
        return name;
    }

    public long getCurrentQuestionId() {
        return currentQuestionId;
    }

    public String getDescription() {
        return description;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentQuestionId(long currentQuestionId) {
        this.currentQuestionId = currentQuestionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "name='" + name + '\'' +
                ", currentQuestionId=" + currentQuestionId +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", questions=" + questions.size() +
                '}';
    }

}
