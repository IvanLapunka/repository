package my.repos;

import my.pojo.AbstractEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRepositoryInMemory<T extends AbstractEntity> implements  Repository<T> {
    protected final Map<Integer, T> map = new ConcurrentHashMap<>();

    protected abstract Integer generateId();

    @Override
    public Set<T> findAll() {
        if (map.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(map.values());
    }

    @Override
    public Optional<T> find(Integer id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public T save(T entity) {
        if (entity == null)
            return null;
        Integer id = entity.getId();
        if (id == null) {
            id = generateId();
            entity.setId(id);
        }
        map.put(id, entity);
        return map.get(id);
    }

    @Override
    public Optional<T> remove(Integer id) {
        return Optional.ofNullable(map.remove(id));
    }
}
