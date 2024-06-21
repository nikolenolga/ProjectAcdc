package lessonsForDelete.lesson9Hibernate;

import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.repository.Repository;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class UserDbDao implements Repository<User> {
    private SessionCreater sessionCreater;

    public UserDbDao(SessionCreater sessionCreater) {
        this.sessionCreater = sessionCreater;
    }

    @Override
    public void create(User user) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void update(User user) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.merge(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(User user) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.remove(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<User> getAll() {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<User> query = session.createQuery("SELECT u FROM User u", User.class);
                Collection<User> users = query.getResultList();
                transaction.commit();
                return users;
            } catch (Exception e) {
                transaction.rollback();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Optional<User> get(Long id) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                User user = session.find(User.class, id);
                transaction.commit();
                return Optional.of(user);
            } catch (Exception e) {
                transaction.rollback();
                return Optional.empty();
            }
        }
    }

    public Stream<User> find(User pattern) {
        Session session = sessionCreater.getSession();
        Class<? extends User> patternClass = pattern.getClass();
        try (session) {
            Transaction tx = session.beginTransaction();
            try {
                //получаем необходимые компоненты для построения запроса на criteria api
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
                Root<User> userRoot = query.from(User.class);
                //получаем поля через рефлекцию
                Field[] fields = patternClass.getDeclaredFields();
                List<Predicate> predicates = new ArrayList<>();
                //для полей не null и не transient собираем список условий через criteriaBuilder
                for (Field field : fields) {
                    field.trySetAccessible();
                    String name = field.getName();
                    Object object = field.get(pattern);
                    if ((object != null) && !field.isAnnotationPresent(Transient.class)) {
                        Predicate predicate = criteriaBuilder.equal(userRoot.get(name), object);
                        predicates.add(predicate);
                    }
                }
                //строим CriteriaQuery на основе select по Root<User> и построенных условий where - Predicate[]
                query.select(userRoot)
                        .where(predicates.toArray(Predicate[]::new));
                //строим обычный Query на основе CriteriaQuery и дальше работаем как обьчно
                Query<User> userQuery = session.createQuery(query);
                Collection<User> users = userQuery.getResultList();
                tx.commit();

                return users.stream();
            } catch (Exception e) {
                tx.rollback();
                throw new QuestException(e);
            }
        }
    }
}
