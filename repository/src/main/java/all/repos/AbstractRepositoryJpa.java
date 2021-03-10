package all.repos;

import by.pojo.AbstractEntity;
import org.hibernate.criterion.CriteriaQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractRepositoryJpa<T extends AbstractEntity> implements Repository<T>{

    private static final String ID = "id";
    protected final EntityManagerHelper helper = EntityManagerHelper.getInstance();

    @Override
    public Set<T> findAll() {
        HashSet<T> resultSet;
        final EntityManager em = helper.getEntityManager();
        TypedQuery<T> tq = getAllItems();
        em.getTransaction().begin();
        resultSet = new HashSet<>(tq.getResultList());
        em.getTransaction().commit();
        em.close();
        return resultSet;
    }

    protected abstract TypedQuery<T> getAllItems();

    @Override
    public Optional<T> find(Integer id) {
        Optional<T> singleResult;
        final EntityManager em = helper.getEntityManager();
        em.getTransaction().begin();
        TypedQuery<T> tq = findItemById();
        singleResult = Optional.ofNullable(tq.setParameter(ID, id).getSingleResult());
        em.getTransaction().commit();
        em.close();
        return singleResult;
    }

    protected abstract TypedQuery<T> findItemById();

    @Override
    public T save(T entity) {
        final EntityManager em = helper.getEntityManager();
        em.getTransaction().begin();

        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Optional<T> remove(Integer id) {
        final EntityManager em = helper.getEntityManager();
        em.getTransaction().begin();
        final Optional<T> toRemove = this.find(id);
        if (!toRemove.isEmpty()) {
            em.remove(toRemove);
        }
        em.getTransaction().commit();
        em.close();
        return toRemove;
    }
}
