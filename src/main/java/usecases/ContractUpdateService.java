package usecases;

import entities.Contract;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import persistance.ContractDAO;

@ApplicationScoped
public class ContractUpdateService {

    @Inject
    private ContractDAO contractDAO;

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
