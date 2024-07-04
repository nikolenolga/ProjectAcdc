package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface Repository<T> {
    Optional<T> create(T t);

    Optional<T> update(T t);

    void delete(T t);

    void delete(Long id);

    Stream<T> getAll();

    Optional<T> get(Long id);

    Stream<T> find(T pattern);
}
