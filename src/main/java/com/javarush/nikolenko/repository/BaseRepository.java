package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.config.SessionCreater;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public abstract class BaseRepository<T> implements Repository<T> {
    private SessionCreater sessionCreater;
    private final Class<T> entityClass;

    @Override
    public Collection<T> getAll() {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<T> query = session.createQuery("SELECT entity FROM %s entity".formatted(entityClass.getName()), entityClass);
                Collection<T> entities = query.getResultList();
                transaction.commit();
                return entities;
            } catch (Exception e) {
                transaction.rollback();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public void create(T entity) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.persist(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void update(T entity) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.merge(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(T entity) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.remove(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Optional<T> get(Long id) {
        try(Session session = sessionCreater.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                T entity = session.find(entityClass, id);
                Optional<T> optionalEntity = Optional.of(entity);
                transaction.commit();
                return optionalEntity;
            } catch (Exception e) {
                transaction.rollback();
                return Optional.empty();
            }
        }
    }

    public Stream<T> find(T pattern) {
        Session session = sessionCreater.getSession();
        //Class<? extends T> patternClass = pattern.getClass();
        try (session) {
            Transaction tx = session.beginTransaction();
            try {
                //получаем необходимые компоненты для построения запроса на criteria api
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
                Root<T> entityRoot = query.from(entityClass);
                //получаем поля через рефлекцию
                Field[] fields = entityClass.getDeclaredFields();
                List<Predicate> predicates = new ArrayList<>();
                //для полей не null и не transient собираем список условий через criteriaBuilder
                for (Field field : fields) {
                    field.trySetAccessible();
                    String name = field.getName();
                    Object object = field.get(pattern);
                    if ((object != null) && !field.isAnnotationPresent(Transient.class)) {
                        Predicate predicate = criteriaBuilder.equal(entityRoot.get(name), object);
                        predicates.add(predicate);
                    }
                }
                //строим CriteriaQuery на основе select по Root<T> и построенных условий where - Predicate[]
                query.select(entityRoot)
                        .where(predicates.toArray(Predicate[]::new));
                //строим обычный Query на основе CriteriaQuery и дальше работаем как обьчно
                Query<T> entityQuery = session.createQuery(query);
                Collection<T> entities = entityQuery.getResultList();
                tx.commit();

                return entities.stream();
            } catch (Exception e) {
                tx.rollback();
                throw new QuestException(e);
            }
        }
    }
}
