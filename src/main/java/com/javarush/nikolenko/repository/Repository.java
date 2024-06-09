package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface Repository<T> {
    void create(T t);

    void update(T t);

    void delete(T t);

    Collection<T> getAll();

    Optional<T> get(Long id);

    Stream<T> find(T pattern);
}
