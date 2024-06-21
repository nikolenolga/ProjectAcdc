package lessonsForDelete.lesson12shmibernate.converter;

import com.javarush.nikolenko.config.ApplicationProperties;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.io.IOException;

//SessionCreater всегда один
public class SessionCreater implements Closeable {
    private final SessionFactory sessionFactory;

    @SneakyThrows
    public SessionCreater() {
        Configuration configuration = new Configuration();

        sessionFactory = configuration.configure()
                .addProperties(new ApplicationProperties())
                        .addAnnotatedClass(Password.class)
                        .addAnnotatedClass(DemoUser.class)
                // true - автоматическое применение к найденным полям
                        .addAttributeConverter(new DemoConverter(), true)
                        .buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void close() throws IOException {
        sessionFactory.close();
    }
}
