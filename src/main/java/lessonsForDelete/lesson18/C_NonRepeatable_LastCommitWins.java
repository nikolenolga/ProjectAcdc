package lessonsForDelete.lesson18;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.GameState;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class C_NonRepeatable_LastCommitWins {
    public static void main(String[] args) {
        SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
        Session session1 = sessionCreater.getSession();
        Session session2 = sessionCreater.getSession();
        Transaction tx1  = session1.beginTransaction();
        Transaction tx2  = session2.beginTransaction();

        Game game1 = session1.find(Game.class, 1L);
        Game game2 = session2.find(Game.class, 1L);
        game1.setGameState(GameState.WIN);
        game2.setGameState(GameState.LOSE);
        tx1.commit();
        tx2.commit();

        System.out.println(sessionCreater.getSession().find(Game.class, 1L));
        /*
        проблема - last commit wins

        логическая ошибка для типичного ожидаемого поведения,
        ожидается что кто первый успел сделать
        */
        session1.close();
        session2.close();
    }
}
