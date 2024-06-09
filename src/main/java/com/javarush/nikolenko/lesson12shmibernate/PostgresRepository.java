package com.javarush.nikolenko.lesson12shmibernate;

import com.javarush.nikolenko.lesson12shmibernate.converter.AbstractEntity;
import com.javarush.nikolenko.lesson12shmibernate.engine.Repository;
import com.javarush.nikolenko.lesson12shmibernate.engine.UniversalRepository;

import java.util.HashMap;
import java.util.Map;

public class PostgresRepository {
    private static final Map<Object, Object> repositories = new HashMap<>();

    static <T extends AbstractEntity> Repository<T> get(Class<T> type) {
        return (Repository<T>) repositories
                .computeIfAbsent(type, t -> new UniversalRepository<>(type, new PostgresDialect()));
    }
}
