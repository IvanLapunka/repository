package all.repos;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;

public class EntityManagerHelper {
    private final SessionFactory factory;

    private EntityManagerHelper() {
        final Configuration configure = new Configuration().configure();
        this.factory = configure.buildSessionFactory();
    }

    private static class EntityManagerHelperHolder {
        private static final EntityManagerHelper INSTANCE_HOLDER = new EntityManagerHelper();
    }

    public static EntityManagerHelper getInstance() {
        return EntityManagerHelperHolder.INSTANCE_HOLDER;
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
