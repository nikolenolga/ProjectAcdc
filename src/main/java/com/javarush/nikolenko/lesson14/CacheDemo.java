package com.javarush.nikolenko.lesson14;

import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import jakarta.persistence.Subgraph;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.query.Query;

import java.util.List;

@NoArgsConstructor
public class CacheDemo {

    public static void main(String[] args) {
        PerfomanceDemo demo = new PerfomanceDemo();
        demo.readWithJoinFetch();
    }

    public void read() {
        @Cleanup SessionCreater sessionCreater = new SessionCreater();
        for (int i = 0; i < 1000; i++) {
            Session session = sessionCreater.getSession();
            try(session) {
                Transaction transaction = session.beginTransaction();
                try {
                    User user = session.get(User.class, 1L);
                    System.out.println(user);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
        }
    }

}
