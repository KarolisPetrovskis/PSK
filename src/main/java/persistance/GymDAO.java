package persistance;

import entities.Gym;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class GymDAO {

    @Inject
    private EntityManager em;

    public List<Gym> loadAll() {
        return em.createQuery("select g from Gym g", Gym.class).getResultList();
    }

    public void persist(Gym gym) {
        this.em.persist(gym);
    }

    public Gym findOne(Long id) {
        return em.find(Gym.class, id);
    }

    public Gym update(Gym gym) {
        return em.merge(gym);
    }

    public List<Gym> loadAllWithTrainers() {
        return em.createQuery(
                        "SELECT DISTINCT g FROM Gym g LEFT JOIN FETCH g.trainers", Gym.class)
                .getResultList();
    }
}