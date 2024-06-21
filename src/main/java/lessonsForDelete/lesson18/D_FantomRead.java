package lessonsForDelete.lesson18;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Game;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class D_FantomRead {

    public static void main(String[] args) {
        SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
        Session session1 = sessionCreater.getSession();
        Session session2 = sessionCreater.getSession();
        Transaction tx1  = session1.beginTransaction();
        Transaction tx2  = session2.beginTransaction();

        int size1 = session1
                .createQuery("SELECT g FROM Game g", Game.class)
                .list().size();

        session2.createQuery("DELETE FROM Game g WHERE g.id = 1").executeUpdate();
        tx2.commit();

        int size2 = session1
                .createQuery("SELECT g FROM Game g", Game.class)
                .list().size();
        tx1.commit();

        System.out.printf("before: %d, after: %d%n".formatted(size1, size2));
    }

}
