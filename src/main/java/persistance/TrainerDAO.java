package persistance;

import entities.Trainer;
import entities.Gym;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class TrainerDAO {

    @Inject
    private EntityManager em;

    public List<Trainer> loadAll() {
        return em.createQuery("select t from Trainer t", Trainer.class).getResultList();
    }

    public void persist(Trainer trainer) {
        this.em.persist(trainer);
    }

    public Trainer findOne(Long id) {
        return em.find(Trainer.class, id);
    }

    public List<Trainer> findByNameAndGym(String name, Gym gym) {
        return em.createQuery("select t from Trainer t where t.name = :name and t.gym = :gym", Trainer.class)
                .setParameter("name", name)
                .setParameter("gym", gym)
                .getResultList();
    }

    public Trainer update(Trainer trainer) {
        return em.merge(trainer);
    }
}