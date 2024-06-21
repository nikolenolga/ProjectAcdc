package lessonsForDelete.lesson18;

import com.javarush.nikolenko.config.Configurator;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class A_LostUpdate {
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
        tx2.rollback();

        System.out.println(sessionCreater.getSession().find(Game.class, 1L).getGameState());
        /*
        нужно разобраться позже, если Game 1L - равен GAME

        демонстрация ошибки LostUpdate
        u1 + u2 + c1 + rb2 - в результате должны потерять c1 - коммит 1ой сессии, так как ролбэк вернет изначальное значение

        скорее всего у нас уровень изоляции по умолчанию 2 и поэтому пример ошибки не срабатывает
        */
        session1.close();
        session2.close();
    }
}
