package all.repos;

import by.pojo.AbstractEntity;
import exceptions.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.CriteriaQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Slf4j
public abstract class AbstractRepositoryJpa<T extends AbstractEntity> implements Repository<T>{

    private static final String ID = "id";
    protected final EntityManagerHelper helper = EntityManagerHelper.getInstance();

    @Override
    public Set<T> findAll() {
        HashSet<T> resultSet = null;
        EntityManager em = null;
        try {
            em = helper.getEntityManager();
            TypedQuery<T> tq = getAllItems();
            em.getTransaction().begin();
            resultSet = new HashSet<>(tq.getResultList());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error during items loading", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return resultSet;
    }

    protected abstract TypedQuery<T> getAllItems();

    @Override
    public Optional<T> find(Integer id) {
        Optional<T> singleResult = Optional.empty();
        EntityManager em = null;
        try {
            em = helper.getEntityManager();
            em.getTransaction().begin();
            TypedQuery<T> tq = findItemById();
            singleResult = Optional.ofNullable(tq.setParameter(ID, id).getSingleResult());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error during loading item by id", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return singleResult;
    }

    protected abstract TypedQuery<T> findItemById();

    @Override
    public T save(T entity) {
        EntityManager em = null;
        try {
            em = helper.getEntityManager();
            em.getTransaction().begin();

            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error during loading item by id", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return entity;
    }

    @Override
    public Optional<T> remove(Integer id) {
        EntityManager em = null;
        Optional<T> toRemove = Optional.empty();
        try {
            em = helper.getEntityManager();
            em.getTransaction().begin();
            toRemove = this.find(id);
            if (!toRemove.isEmpty()) {
                em.remove(toRemove);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error during loading item by id", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return toRemove;
    }
}
