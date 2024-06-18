package com.javarush.lessonsForDelete.lesson11;

import com.javarush.nikolenko.config.ApplicationProperties;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.config.SessionCreater;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.SessionStatistics;

public class HibernateContext {

    @SneakyThrows
    public static void main(String[] args) {
        SessionCreater sessionCreater = new SessionCreater(new ApplicationProperties());
        Session session = sessionCreater.getSession();
        try(session) {
            Transaction transaction = session.beginTransaction();
            try {
                User bob = session.find(User.class, 4L);
                print(session);
                bob.setPassword("47");
                print(session);

                transaction.commit();
                bob.setPassword("123");
                print(session);
                System.out.println(bob);
            } catch (Exception e) {
                transaction.rollback();
                throw new QuestException(e);
            }
        }
        sessionCreater.close();
    }

    private static void print(Session session) {
        boolean dirty = session.isDirty();
        SessionStatistics statistics = session.getStatistics();
        String name = session.toString();
        String line = "-".repeat(60);
        System.out.printf("%s%nName: %s%nDirty: %s%n:Stat: %s%n%s%n", line, name, dirty, statistics, line);
    }
}
