package all.repos;

import by.pojo.Group;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class GroupRepositoryJpa extends AbstractRepositoryJpa<Group> implements GroupRepository{

    private GroupRepositoryJpa() {
    }

    private static class GroupRepositoryJpaHolder {
        private static final GroupRepositoryJpa INSTANCE_HOLDER = new GroupRepositoryJpa();
    }

    public static GroupRepositoryJpa getInstance() {
        return GroupRepositoryJpaHolder.INSTANCE_HOLDER;
    }

    @Override
    protected TypedQuery<Group> getAllItems() {
        final EntityManager em = helper.getEntityManager();
        final TypedQuery<Group> query = em.createQuery("from Group", Group.class);
        return query;
    }

    @Override
    protected TypedQuery<Group> findItemById() {
        final EntityManager em = helper.getEntityManager();
        final TypedQuery<Group> query = em.createQuery("from Group where id = :id", Group.class);
        return query;
    }
}
