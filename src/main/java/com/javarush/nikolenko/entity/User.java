package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString
public class User extends AbstractComponent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private final Collection<Quest> quests = new ArrayList<>();

    @OneToMany(mappedBy = "player")
    @ToString.Exclude
    private final Collection<Game> games = new ArrayList<>();

    @Override
    public String getImage() {
        return super.getImage()  + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 42;
    }

    public void addQuest(Quest quest) {
        quest.setAuthor(this);
        quests.add(quest);
    }
}
