package lessonsForDelete.lesson14;

import com.javarush.nikolenko.config.ApplicationProperties;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.User;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;

@NoArgsConstructor
public class CacheDemo {

    public static void main(String[] args) {
        PerfomanceDemo demo = new PerfomanceDemo();
        demo.readWithJoinFetch();
    }

    @SneakyThrows
    public void read() {
        @Cleanup SessionCreater sessionCreater = new SessionCreater(new ApplicationProperties());
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
