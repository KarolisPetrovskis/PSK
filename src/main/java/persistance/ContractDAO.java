package persistance;

import entities.Contract;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ContractDAO {

    @Inject
    private EntityManager em;

    public List<Contract> loadAll() {
        return em.createQuery(
                        "SELECT c FROM Contract c JOIN FETCH c.client ORDER BY c.id",
                        Contract.class)
                .getResultList();
    }

    public Contract findOne(Long id) {
        return em.find(Contract.class, id);
    }

    public Contract findOneWithOptimisticLock(Long id) {
        return em.find(Contract.class, id, LockModeType.OPTIMISTIC);
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void persist(Contract contract) {
        em.persist(contract);
    }

    public Contract update(Contract contract) {
        return em.merge(contract);
    }

    public void flush() {
        em.flush();
    }

    public void clear() {
        em.clear();
    }
}
