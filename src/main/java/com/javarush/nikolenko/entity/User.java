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
@AllArgsConstructor
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

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }

}
