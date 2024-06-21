package lessonsForDelete.lesson15;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Runner {
    public static void main(String[] args) {
        Session session = Database.SESSION.open();

        try (session) {
            Transaction tx = session.beginTransaction();
            try{
                BaseParent baseParent = BaseParent.builder()
                        .name("baseName")
                        .email("baseEmail")
                        .build();
                session.persist(baseParent);

                Seller seller = Seller.builder()
                        .name("sellerName")
                        .email("sellerEmail")
                        .profit(1255)
                        .build();
                session.persist(seller);


                Customer customer = Customer.builder()
                        .name("customerName")
                        .email("customerEmail")
                        .orderCount(55)
                        .build();
                session.persist(customer);



                System.out.println(baseParent);
                System.out.println(seller);
                System.out.println(customer);

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
            }
        }
    }

}

enum Database {
    SESSION;

    private Session session;
    private final SessionFactory sessionFactory;

    Session open() {
        session = sessionFactory.openSession();
        return session;
    }

    void close() {
        session.close();
    }

    void closeFactory(){
        sessionFactory.close();
    }

    Database() {
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger("");
        logger.setLevel(Level.WARNING);

        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/game")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "postgres")
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.generate_statistics", "false")
                //автоматическое создание таблиц, сначала дропнется если есть, потом пересоздадутся
                .setProperty("hibernate.hbm2ddl.auto", "create");

        configuration.addAnnotatedClass(BaseParent.class);
        configuration.addAnnotatedClass(Seller.class);
        configuration.addAnnotatedClass(Customer.class);

        sessionFactory = configuration.buildSessionFactory();
    }
}