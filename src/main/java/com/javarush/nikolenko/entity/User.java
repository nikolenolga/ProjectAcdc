package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class User implements AbstractComponent, Serializable {
//    @Serial
//    private static final long serialVersionUID = -1798070786993154676L;

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

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    @ToString.Exclude
    private final Collection<Quest> quests = new ArrayList<>();

    @OneToMany(mappedBy = "player")
    @ToString.Exclude
    private final Collection<Game> games = new ArrayList<>();

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
