package com.javarush.nikolenko.dto;

import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class UserTo {
    Long id;
    String name;
    String login;
    //String password;
    Role role;
    Collection<Quest> quests;
    Collection<Game> games;
}
