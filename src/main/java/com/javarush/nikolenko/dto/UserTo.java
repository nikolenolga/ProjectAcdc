package com.javarush.nikolenko.dto;

import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.Quest;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Builder
public class UserTo {
    Long id;
    String name;
    String login;
    String password;
    Role role;
    String image;
    List<QuestTo> quests;
}
