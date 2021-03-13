package all.repos;

import by.pojo.AbstractEntity;

import java.util.Optional;
import java.util.Set;

public interface Repository<T extends AbstractEntity> {
    Set<T>  findAll();
    Optional<T> find(Integer id);
    T save(T entity);
    Optional<T> remove(Integer id);
}
