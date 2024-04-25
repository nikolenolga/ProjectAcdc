package com.javarush.nikolenko.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractComponent {
    private final List<Quest> createdQuests = new ArrayList<>();
    private final List<Game> playedGames = new ArrayList<>();
    private String name;
    private String login;
    private String password;

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
}
