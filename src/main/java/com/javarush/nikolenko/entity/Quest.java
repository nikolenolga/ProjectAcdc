package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quest")
@ToString
@NamedQueries({
        @NamedQuery(name = "QUERY_MORE_ID", query = "SELECT q FROM Quest q where id>:id")
})
@FetchProfile(
        name = Quest.FETCH_LAZY_QUESTIONS_AND_JOIN_AUTHOR,
        fetchOverrides = {
                @FetchProfile.FetchOverride(
                        entity = Quest.class,
                        association = "questions",
                        mode = FetchMode.JOIN
                ),
                @FetchProfile.FetchOverride(
                        entity = Quest.class,
                        association = "author",
                        mode = FetchMode.JOIN
                )
        }
)
@NamedEntityGraph(
        name = Quest.GRAPH_LAZY_QUESTIONS_AND_JOIN_AUTHOR,
        attributeNodes = {
                @NamedAttributeNode(value = "questions", subgraph = "questions")
        },
        subgraphs = {
                @NamedSubgraph(name = "questions", attributeNodes = {})
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Quest implements AbstractComponent, Serializable {
    @Serial
    private static final long serialVersionUID = -179807072993154676L;

    public static final String GRAPH_LAZY_QUESTIONS_AND_JOIN_AUTHOR = "graphLazyQuestionsAndJoinAuthor";
    public static final String FETCH_LAZY_QUESTIONS_AND_JOIN_AUTHOR = "fetchLazyQuestionsAndJoinAuthor";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_author_id")
    @ToString.Exclude
    private User author;

    @Column(name = "first_question_id")
    private Long firstQuestionId;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "quest")
    @ToString.Exclude
    private final List<Question> questions = new ArrayList<>();


    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return Objects.equals(id, quest.id);
    }

    @Override
    public int hashCode() {
        return 7;
    }
}
