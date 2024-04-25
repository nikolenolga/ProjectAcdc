package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.AbstractComponent;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseRepository<T extends AbstractComponent> implements Repository<T> {
    protected final Map<Long, T> map = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0L);

    @Override
    public void create(T component) {
        component.setId(id.incrementAndGet());
        update(component);
    }

    @Override
    public void update(T component) {
        map.put(component.getId(), component);
    }

    @Override
    public void delete(T component) {
        map.remove(component.getId());
    }

    @Override
    public Collection<T> getAll() {
        return map.values();
    }

    @Override
    public Optional<T> get(Long id) {
        return Optional.ofNullable(map.get(id));
    }

}
