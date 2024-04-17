package com.javarush.nikolenko.entity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User extends AbstractComponent {
    private String name;
    private String login;
    private String password;
    private final List<Quest> createdQuests = new ArrayList<>();
    private final List<Game> playedGames = new ArrayList<>();

    public User() {}

    public User(String name) {
        super(0L);
        this.name = name;
    }

    public User(String name, String login, String password) {
        super(0L);
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public void addQuest(Quest quest) {
        createdQuests.add(quest);
    }

    public List<Quest> getCreatedQuests() {
        return Collections.unmodifiableList(createdQuests);
    }

    public void addQuest(Game game) {
        playedGames.add(game);
    }

    public List<Game> getPlayedGames() {
        return Collections.unmodifiableList(playedGames);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
