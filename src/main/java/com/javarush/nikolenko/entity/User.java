package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractComponent {
    private String name;
    private String login;
    private String password;

    public User(String name, String login, String password) {
        super(0L);
        this.name = name;
        this.login = login;
        this.password = password;
        log.debug("New User entity created, id - {}, name - {}, login - {}", id, name, login);
    }

}
