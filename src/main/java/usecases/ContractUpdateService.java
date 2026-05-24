package usecases;

import entities.Client;
import entities.Contract;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import persistance.ClientDAO;
import persistance.ContractDAO;

import java.time.LocalDate;

@ApplicationScoped
public class ContractUpdateService {

    @Inject
    private ContractDAO contractDAO;

    @Inject
    private ClientDAO clientDAO;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Long saveLoadedContract(
            Long contractId,
            Long contractVersion,
            Long clientId,
            LocalDate startsOn,
            String membershipType,
            boolean active
    ) {
        Client client = clientDAO.findOne(clientId);

        if (client == null) {
            throw new IllegalArgumentException("Client of loaded contract was not found.");
        }

        Contract detachedContract = new Contract();
        detachedContract.setId(contractId);
        detachedContract.setVersion(contractVersion);
        detachedContract.setClient(client);
        detachedContract.setStartsOn(startsOn);
        detachedContract.setMembershipType(membershipType);
        detachedContract.setActive(active);

        Contract savedContract = contractDAO.update(detachedContract);
        contractDAO.flush();

        return savedContract.getVersion();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateContractInSeparateTransaction(Long contractId, String newMembershipType) {
        Contract contract = contractDAO.findOne(contractId);

        if (contract == null) {
            throw new IllegalArgumentException("Contract not found: " + contractId);
        }

        contract.setMembershipType(newMembershipType);
        contractDAO.flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void reloadAndSaveAfterOptimisticLockException(Long contractId, String recoveredMembershipType) {
        Contract freshContract = contractDAO.findOne(contractId);

        if (freshContract == null) {
            throw new IllegalArgumentException("Contract not found: " + contractId);
        }

        freshContract.setMembershipType(recoveredMembershipType);
        contractDAO.flush();
    }
}
