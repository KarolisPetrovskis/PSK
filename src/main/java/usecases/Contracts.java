package usecases;

import entities.Client;
import entities.Contract;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import persistance.ClientDAO;
import persistance.ContractDAO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@SessionScoped
public class Contracts implements Serializable {

    @Inject
    private ContractDAO contractDAO;

    @Inject
    private ClientDAO clientDAO;

    @Getter
    private List<Contract> allContracts;

    @Getter
    private List<Client> allClients;

    @Getter @Setter
    private Long clientIdForNewContract;

    @Getter @Setter
    private String membershipTypeForNewContract = "STANDARD";

    @Getter @Setter
    private Long selectedContractId;

    @Getter
    private Long loadedContractId;

    @Getter
    private Long loadedContractVersion;

    @Getter
    private Long loadedContractClientId;

    @Getter
    private String loadedContractClientName;

    @Getter
    private LocalDate loadedContractStartsOn;

    @Getter @Setter
    private String editedMembershipType;

    @Getter @Setter
    private boolean editedActive;

    @Getter
    private String optimisticLockDemoMessage;

    @PostConstruct
    public void init() {
        loadData();
    }

    @Transactional
    public String createContract() {
        Client client = clientDAO.findOne(clientIdForNewContract);

        if (client == null) {
            optimisticLockDemoMessage = "Client was not found.";
            return null;
        }

        Contract contract = new Contract(
                client,
                membershipTypeForNewContract,
                LocalDate.now()
        );

        contractDAO.persist(contract);

        clearEditingForm();
        loadData();
        return null;
    }

    public String loadSelectedContractForEditing() {
        optimisticLockDemoMessage = null;

        Contract contract = contractDAO.findOne(selectedContractId);

        if (contract == null) {
            optimisticLockDemoMessage = "Contract was not found.";
            clearEditingForm();
            return null;
        }

        loadedContractId = contract.getId();
        loadedContractVersion = contract.getVersion();
        loadedContractClientId = contract.getClient().getId();
        loadedContractClientName = contract.getClient().getName();
        loadedContractStartsOn = contract.getStartsOn();
        editedMembershipType = contract.getMembershipType();
        editedActive = contract.isActive();

        optimisticLockDemoMessage = "";

        return null;
    }

    @Transactional
    public String saveLoadedContract() {
        optimisticLockDemoMessage = null;

        if (loadedContractId == null) {
            optimisticLockDemoMessage = "First load a contract for editing.";
            return null;
        }

        Client client = clientDAO.findOne(loadedContractClientId);

        if (client == null) {
            optimisticLockDemoMessage = "Client of loaded contract was not found.";
            return null;
        }

        Contract detachedContract = new Contract();
        detachedContract.setId(loadedContractId);
        detachedContract.setVersion(loadedContractVersion);
        detachedContract.setClient(client);
        detachedContract.setStartsOn(loadedContractStartsOn);
        detachedContract.setMembershipType(editedMembershipType);
        detachedContract.setActive(editedActive);

        try {
            Contract savedContract = contractDAO.update(detachedContract);
            contractDAO.flush();

            loadedContractVersion = savedContract.getVersion();

            optimisticLockDemoMessage =
                    "Contract was saved successfully.\n" +
                    "New version: " + loadedContractVersion;

        } catch (OptimisticLockException exception) {
            contractDAO.clear();

            optimisticLockDemoMessage =
                    "OptimisticLockException was caught.\n\n" +
                    "The form tried to save stale version: " + loadedContractVersion + ".\n\n" +
                    "Exception type:\n" +
                    exception.getClass().getName() + "\n\n" +
                    "Exception message:\n" +
                    exception.getMessage() + "\n\n";

            clearEditingForm();
        }

        return null;
    }

    private void clearEditingForm() {
        selectedContractId = null;
        loadedContractId = null;
        loadedContractVersion = null;
        loadedContractClientId = null;
        loadedContractClientName = null;
        loadedContractStartsOn = null;
        editedMembershipType = null;
        editedActive = false;
    }

    private void loadData() {
        this.allContracts = contractDAO.loadAll();
        this.allClients = clientDAO.loadAll();
    }
}
