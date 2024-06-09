package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.config.SessionCreater;

public class GameRepository extends BaseRepository<Game> {

    public GameRepository(SessionCreater sessionCreater) {
        super(sessionCreater, Game.class);
    }
}
