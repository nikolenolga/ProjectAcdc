package com.javarush.nikolenko.repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {
    void create(T t);

    void update(T t);

    void delete(T t);

    Collection<T> getAll();

    Optional<T> get(long id);

}
