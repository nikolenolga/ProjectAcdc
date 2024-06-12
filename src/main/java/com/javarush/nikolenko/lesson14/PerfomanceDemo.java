package com.javarush.nikolenko.lesson14;

import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import jakarta.persistence.Subgraph;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.query.Query;

import java.util.List;

@NoArgsConstructor
public class PerfomanceDemo {
    public static void main(String[] args) {
        PerfomanceDemo demo = new PerfomanceDemo();
        demo.readWithJoinFetch();
    }

    //EntityGraph demo- граф программный без аннотаций - загрузка всех квестов с вопросами и с автором и юзеринфо за один запрос
    public void readWithEntityGraphNoAnnotation() {
        SessionCreater sessionCreater = new SessionCreater();
        Session session = sessionCreater.getSession();
        try(session) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Quest> questQuery = session.createQuery(
                        "select q from Quest q", Quest.class);

                String hintName = GraphSemantic.FETCH.getJakartaHintName();
                RootGraph<Quest> questRootGraph = session.createEntityGraph(Quest.class);
                questRootGraph.addAttributeNodes("questions", "author");
                questRootGraph.addSubgraph("questions");

                Subgraph<User> authorGraph = questRootGraph.addSubgraph("author", User.class);
                authorGraph.addAttributeNodes("userInfo");

                questQuery.setHint(hintName, questRootGraph);

                List<Quest> list = questQuery.list();
                for (Quest quest : list) {
                    User author = quest.getAuthor();
                    System.out.println(quest);
                    System.out.println(quest.getQuestions());
                    System.out.println(author);
                    System.out.println(author.getUserInfo());
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    //EntityGraph demo- граф на аннотациях - загрузка всех квестов с вопросами и с автором и юзеринфо за один запрос
    public void readWithAnnotatedEntityGraph() {
        SessionCreater sessionCreater = new SessionCreater();
        Session session = sessionCreater.getSession();
        try(session) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Quest> questQuery = session.createQuery(
                        "select q from Quest q", Quest.class);

                String hintName = GraphSemantic.FETCH.getJakartaHintName();
                RootGraph<?> graph = session.createEntityGraph(Quest.GRAPH_LAZY_QUESTIONS_AND_JOIN_AUTHOR_AND_JOIN_USERINFO);
                questQuery.setHint(hintName, graph);

                List<Quest> list = questQuery.list();
                for (Quest quest : list) {
                    User author = quest.getAuthor();
                    System.out.println(quest);
                    System.out.println(quest.getQuestions());
                    System.out.println(author);
                    System.out.println(author.getUserInfo());
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    //fetch profile demo - загрузка всех квестов с вопросами и с автором и юзеринфо за один запрос
    public void readWithFetchProfile() {
        SessionCreater sessionCreater = new SessionCreater();
        Session session = sessionCreater.getSession();
        try(session) {
            Transaction transaction = session.beginTransaction();
            session.enableFetchProfile(Quest.FETCH_LAZY_QUESTIONS_AND_JOIN_AUTHOR_AND_JOIN_USERINFO);
            try {
                Query<Quest> questQuery = session.createQuery(
                        "select q from Quest q", Quest.class);
                List<Quest> list = questQuery.list();
                for (Quest quest : list) {
                    User author = quest.getAuthor();
                    System.out.println(quest);
                    System.out.println(quest.getQuestions());
                    System.out.println(author);
                    System.out.println(author.getUserInfo());
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    //join fetch demo - загрузка всех квестов с вопросами за один запрос
    public void readWithJoinFetch() {
        SessionCreater sessionCreater = new SessionCreater();
        Session session = sessionCreater.getSession();
        try(session) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Quest> questQuery = session.createQuery(
                        "select q from Quest q join fetch q.questions",
                        Quest.class);
                List<Quest> list = questQuery.list();
                for (Quest quest : list) {
                    System.out.println(quest);
                    System.out.println(quest.getQuestions());
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }
}
