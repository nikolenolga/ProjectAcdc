package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString(exclude = {"quests", "games"})
public class User extends AbstractComponent {
    @Transient
    private final Collection<Quest> quests = new ArrayList<>();
    @Transient
    private final Collection<Game> games = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
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

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }

}
