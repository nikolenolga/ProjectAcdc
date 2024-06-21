package lessonsForDelete.lesson10HQL;

import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.config.SessionCreater;
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
import java.util.*;
import java.util.stream.Stream;

public class QuestDbDao implements Repository<Quest> {
    private SessionCreater sessionCreater;

    public QuestDbDao(SessionCreater sessionCreater) {
        this.sessionCreater = sessionCreater;
    }

    @Override
    public void create(Quest quest) {
        try (Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(quest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void update(Quest quest) {
        try (Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(quest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(Quest quest) {
        try (Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(quest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Collection<Quest> getAll() {
        try (Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Quest> query = session.createQuery("SELECT q FROM Quest q", Quest.class);
                Collection<Quest> quests = query.getResultList();
                transaction.commit();
                return quests;
            } catch (Exception e) {
                transaction.rollback();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Optional<Quest> get(Long id) {
        try (Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Quest quest = session.find(Quest.class, id);
                transaction.commit();
                return Optional.of(quest);
            } catch (Exception e) {
                transaction.rollback();
                return Optional.empty();
            }
        }
    }

    public Stream<Quest> find(Quest pattern) {
        Session session = sessionCreater.getSession();
        Class<? extends Quest> patternClass = pattern.getClass();
        try (session) {
            Transaction tx = session.beginTransaction();
            try {
                //получаем необходимые компоненты для построения запроса на criteria api
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<Quest> query = criteriaBuilder.createQuery(Quest.class);
                Root<Quest> questRoot = query.from(Quest.class);
                //получаем поля через рефлекцию
                Field[] fields = patternClass.getDeclaredFields();
                List<Predicate> predicates = new ArrayList<>();
                //для полей не null и не transient собираем список условий через criteriaBuilder
                for (Field field : fields) {
                    field.trySetAccessible();
                    String name = field.getName();
                    Object object = field.get(pattern);
                    if(object != null && !field.isAnnotationPresent(Transient.class)) {
                        Predicate predicate = criteriaBuilder.equal(questRoot.get(name), object);
                        predicates.add(predicate);
                    }
                }
                //строим CriteriaQuery на основе select по Root<User> и построенных условий where - Predicate[]
                query.select(questRoot)
                        .where(predicates.toArray(Predicate[]::new));
                //строим обычный Query на основе CriteriaQuery и дальше работаем как обьчно
                Query<Quest> questQuery = session.createQuery(query);
                Collection<Quest> quests = questQuery.getResultList();
                tx.commit();
                return quests.stream();
            } catch (Exception e) {
                tx.rollback();
                throw new QuestException(e);
            }
        }
    }
}
