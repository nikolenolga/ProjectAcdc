package lessonsForDelete.lesson12shmibernate;

import lessonsForDelete.lesson12shmibernate.converter.AbstractEntity;
import lessonsForDelete.lesson12shmibernate.engine.Repository;
import lessonsForDelete.lesson12shmibernate.engine.UniversalRepository;

import java.util.HashMap;
import java.util.Map;

public class PostgresRepository {
    private static final Map<Object, Object> repositories = new HashMap<>();

    static <T extends AbstractEntity> Repository<T> get(Class<T> type) {
        return (Repository<T>) repositories
                .computeIfAbsent(type, t -> new UniversalRepository<>(type, new PostgresDialect()));
    }
}
