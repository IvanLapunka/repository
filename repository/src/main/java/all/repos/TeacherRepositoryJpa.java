package all.repos;

import by.pojo.Teacher;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class TeacherRepositoryJpa extends AbstractRepositoryJpa<Teacher> implements TeacherRepository{

    private TeacherRepositoryJpa() {
    }

    private static class TeacherRepositoryJpaHolder {
        private static final TeacherRepositoryJpa INSTANCE_HOLDER = new TeacherRepositoryJpa();
    }

    public static TeacherRepositoryJpa getInstance() {
        return TeacherRepositoryJpaHolder.INSTANCE_HOLDER;
    }

    @Override
    protected TypedQuery<Teacher> getAllItems() {
        final EntityManager em = helper.getEntityManager();
        final TypedQuery<Teacher> query = em.createQuery("from Teacher", Teacher.class);
        em.close();
        return query;
    }

    @Override
    protected TypedQuery<Teacher> findItemById() {
        final EntityManager em = helper.getEntityManager();
        final TypedQuery<Teacher> query = em.createQuery("from Teacher t where t.id = :id", Teacher.class);
        em.close();
        return query;
    }
}
