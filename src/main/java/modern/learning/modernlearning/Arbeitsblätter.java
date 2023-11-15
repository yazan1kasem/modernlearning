package modern.learning.modernlearning;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Arbeitsblätter {
    EntityManager emf= Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();
}
