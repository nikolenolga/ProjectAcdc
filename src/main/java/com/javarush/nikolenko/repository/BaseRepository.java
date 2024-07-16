package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.config.SessionCreater;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<T> implements Repository<T> {
    protected final SessionCreater sessionCreater;
    private final Class<T> entityClass;

    @Override
    public Collection<T> getAll() {
        Session session = sessionCreater.getSession();
        Query<T> query = session.createQuery("SELECT entity FROM %s entity".formatted(entityClass.getName()),
                entityClass);
        return query.list();
    }

    @Override
    public Optional<T> create(T entity) {
        Session session = sessionCreater.getSession();
        session.saveOrUpdate(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<T> update(T entity) {
        Session session = sessionCreater.getSession();
        session.merge(entity);
        return Optional.of(entity);
    }

    @Override
    public void delete(T entity) {
        Session session = sessionCreater.getSession();
        session.remove(entity);
    }

    @Override
    public boolean delete(long id) {
        Optional<T> optionalEntity = get(id);
        if (optionalEntity.isPresent()) {
            delete(optionalEntity.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<T> get(long id) {
        Session session = sessionCreater.getSession();
        T entity = session.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

}
