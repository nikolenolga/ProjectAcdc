package com.javarush.nikolenko.entity;

import com.javarush.nikolenko.dto.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@ToString
@Cacheable
public class User implements AbstractComponent {

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
    private final List<Quest> quests = new ArrayList<>();

    @OneToMany(mappedBy = "player")
    @ToString.Exclude
    private final List<Game> games = new ArrayList<>();

    public Collection<Quest> getQuests() {
        return Collections.unmodifiableList(quests);
    }

    public Collection<Game> getGames() {
        return Collections.unmodifiableList(games);
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
