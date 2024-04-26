package modern.learning.modernlearning;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

abstract public class DatabaseConnection {
    static private EntityManager em= Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();
    public static EntityManager getConnection(){
        return em;
    }
}
