package persistance;

import entities.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class ClientDAO {

    @Inject
    private EntityManager em;

    public List<Client> loadAll() {
        return em.createQuery("select c from Client c", Client.class).getResultList();
    }

    public void persist(Client client) {
        this.em.persist(client);
    }

    public Client findOne(Long id) {
        return em.find(Client.class, id);
    }

    public Client update(Client client) {
        return em.merge(client);
    }

    public List<Client> loadAllWithTrainers() {
        return em.createQuery(
                        "SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.trainers", Client.class)
                .getResultList();
    }
}