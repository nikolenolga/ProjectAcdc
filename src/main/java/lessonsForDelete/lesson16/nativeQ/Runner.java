package lessonsForDelete.lesson16.nativeQ;

import com.javarush.nikolenko.config.ApplicationProperties;
import com.javarush.nikolenko.config.SessionCreater;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.NativeQuery;


public class Runner {
    public static void main(String[] args) {
        SessionCreater sessionCreater = new SessionCreater(new ApplicationProperties());
        Session session = sessionCreater.getSession();
        try(session){
            Transaction transaction = session.beginTransaction();
            try {
                NativeQuery<UnMappedDemo> nativeQuery = session.createNativeQuery(
                        "select m.id, m.name from mapped_demo m",
                        "NameTitleMapping");
                nativeQuery.list().forEach(System.out::println);

                transaction.commit();
            }catch (Exception e) {
                transaction.rollback();
            }
        }

        Runner runner = new Runner();
        runner.SqlResultSetMappingDemo();
        runner.criteriaDaoDemo();
    }

    public void criteriaDaoDemo() {
        SessionCreater sessionCreater = new SessionCreater(new ApplicationProperties());
        Session session = sessionCreater.getSession();
        try(session){
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<UnMappedDemo> criteriaQuery = criteriaBuilder.createQuery(UnMappedDemo.class);
                Root<MappedDemo> root = criteriaQuery.from(MappedDemo.class);

                criteriaQuery.multiselect(
                        root.get("id").alias("id"),
                        //если имя поля отличается - ставим алиас
                        root.get("name").alias("name") );

                Query<UnMappedDemo> query = session.createQuery(criteriaQuery);
                query.list().forEach(System.out::println);

                transaction.commit();
            }catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    public void SqlResultSetMappingDemo() {
        SessionCreater sessionCreater = new SessionCreater(new ApplicationProperties());
        Session session = sessionCreater.getSession();
        try(session){
            Transaction transaction = session.beginTransaction();
            try {
                NativeQuery<UnMappedDemo> nativeQuery = session.createNativeQuery(
                        "select m.id, m.name from mapped_demo m",
                        "NameTitleMapping");
                nativeQuery.list().forEach(System.out::println);

                transaction.commit();
            }catch (Exception e) {
                transaction.rollback();
            }
        }
    }
}
