package all.repos;

import by.pojo.Student;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class StudentRepositoryJpa extends AbstractRepositoryJpa<Student> implements StudentRepository{
    private StudentRepositoryJpa(){
    }

    private static class StudentRepositoryJpaHolder{
        private static final StudentRepositoryJpa INSTANCE_HOLDER = new StudentRepositoryJpa();
    }

    public static StudentRepositoryJpa getInstance() {
        return StudentRepositoryJpaHolder.INSTANCE_HOLDER;
    }

    @Override
    protected TypedQuery<Student> getAllItems() {
        final EntityManager em = helper.getEntityManager();
        final TypedQuery<Student> query = em.createQuery("from Student", Student.class);
        em.close();
        return query;
    }

    @Override
    protected TypedQuery<Student> findItemById() {
        final EntityManager em = helper.getEntityManager();
        final TypedQuery<Student> query = em.createQuery("from Student s where s.id = :id", Student.class);
        em.close();
        return query;
    }
}
