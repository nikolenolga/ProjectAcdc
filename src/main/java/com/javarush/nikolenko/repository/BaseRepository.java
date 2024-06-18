package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.config.SessionCreater;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
        Session session = sessionCreater.getSession();
        Query<T> query = session.createQuery("SELECT entity FROM %s entity".formatted(entityClass.getName()), entityClass);
        return query.getResultList();
    }

    @Override
    public void create(T entity) {
        Session session = sessionCreater.getSession();
        session.saveOrUpdate(entity);
    }

    @Override
    public void update(T entity) {
        Session session = sessionCreater.getSession();
        session.merge(entity);
    }

    @Override
    public void delete(T entity) {
        Session session = sessionCreater.getSession();
        session.remove(entity);
    }

    @Override
    public Optional<T> get(Long id) {
        Session session = sessionCreater.getSession();
        T entity = session.find(entityClass, id);
        return  Optional.of(entity);
    }

    @Override
    @SneakyThrows
    public Stream<T> find(T pattern) {
        Session session = sessionCreater.getSession();
        //Class<? extends T> patternClass = pattern.getClass();
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
                    if (isPredicate(field, object)) {
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
                return entities.stream();
    }

    private static boolean isPredicate(Field field, Object value) {
        return (value != null)
                && !field.isAnnotationPresent(Transient.class)
                && !field.isAnnotationPresent(OneToMany.class)
                && !field.isAnnotationPresent(ManyToOne.class)
                && !field.isAnnotationPresent(OneToOne.class)
                && !field.isAnnotationPresent(ManyToMany.class);
    }
}
