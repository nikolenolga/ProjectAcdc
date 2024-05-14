package com.javarush.nikolenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Builder
public class User extends AbstractComponent {
    private String name;
    private String login;
    private String password;
    private Role role;

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        log.debug("New User entity created, id - {}, name - {}, login - {}", id, name, login);
    }

    public User(Long id, String name, String login, String password, Role role) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
        log.debug("New User entity created, id - {}, name - {}, login - {}, role - {}", id, name, login, role);
    }

    public User(String name, String login, String password, Role role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
        log.debug("New User entity created, name - {}, login - {}, role - {}", name, login, role);
    }

}
