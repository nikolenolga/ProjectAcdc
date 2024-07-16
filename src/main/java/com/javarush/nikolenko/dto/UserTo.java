package com.javarush.nikolenko.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

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
