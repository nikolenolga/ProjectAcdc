package lessonsForDelete.lesson18;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Game;
import com.javarush.nikolenko.entity.GameState;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class C_NonRepeatable {
    public static void main(String[] args) {
        SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
        Session session1 = sessionCreater.getSession();
        Session session2 = sessionCreater.getSession();
        Transaction tx1  = session1.beginTransaction();
        Transaction tx2  = session2.beginTransaction();

        Game game1 = session1.find(Game.class, 1L);
        Game game2 = session2.find(Game.class, 1L);
        System.out.println("1 - " + game1.getGameState());

        game2.setGameState(GameState.WIN);
        session2.getTransaction().commit();
        session1.refresh(game1);

        Game gameAfterUpdate = session1.find(Game.class, 1L);
        System.out.println("2 - " + gameAfterUpdate.getGameState());

        System.out.println("3 - from sc - " + sessionCreater.getSession().find(Game.class, 1L).getGameState());

        /*
        если не сделать рефреш - пример не срабатывает так как лезет в контекст
        пример репитабл рид

        в рамках одной сессии делается два селекта одинаковых - но получаются разные данные,
        потому что между двумя селектами другая сессия изменила данные
        */
        session1.close();
        session2.close();
    }
}
