package com.javarush.nikolenko.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface Repository<T> {
    Optional<T> create(T t);

    Optional<T> update(T t);

    void delete(T t);

    void delete(long id);

    Collection<T> getAll();

    Optional<T> get(long id);

    Stream<T> find(T pattern);
}
