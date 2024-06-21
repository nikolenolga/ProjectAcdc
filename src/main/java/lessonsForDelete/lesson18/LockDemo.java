package lessonsForDelete.lesson18;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.GameState;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.lock.OptimisticEntityLockException;

public class LockDemo {
    public static void main(String[] args) {
        SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
        Session session1 = sessionCreater.getSession();
        Transaction tx1 = session1.beginTransaction();

        Game game = session1.find(Game.class, 2L, LockModeType.OPTIMISTIC);
        game.setCurrentQuestionId(199L);
        tx1.commit();

        System.out.printf("DB: " + sessionCreater.getSession().find(Game.class, 2L));

        demo_NonRepeatable_LastCommitWins();
    }

    public static void demo_NonRepeatable_LastCommitWins() {
        SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
        Session session1 = sessionCreater.getSession();
        Session session2 = sessionCreater.getSession();
        Transaction tx1  = session1.beginTransaction();
        Transaction tx2  = session2.beginTransaction();
        try {
            Game game1 = session1.find(Game.class, 2L, LockModeType.OPTIMISTIC);
            Game game2 = session2.find(Game.class, 2L, LockModeType.OPTIMISTIC);
            game1.setGameState(GameState.WIN);
            game2.setGameState(GameState.LOSE);

            tx1.commit();
            tx2.commit();
        } catch (OptimisticEntityLockException|OptimisticLockException e) {
            System.out.println("GameState был изменен другой транзакцией");
        }

        System.out.println(sessionCreater.getSession().find(Game.class, 2L));

        session1.close();
        session2.close();
    }
}
