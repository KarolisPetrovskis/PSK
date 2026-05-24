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

    @Inject
    private ContractUpdateService contractUpdateService;

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
            clearEditingForm();
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

    public String saveLoadedContract() {
        optimisticLockDemoMessage = null;

        if (loadedContractId == null) {
            optimisticLockDemoMessage = "First load a contract for editing.";
            return null;
        }

        try {
            loadedContractVersion = contractUpdateService.saveLoadedContract(
                    loadedContractId,
                    loadedContractVersion,
                    loadedContractClientId,
                    loadedContractStartsOn,
                    editedMembershipType,
                    editedActive
            );

            optimisticLockDemoMessage =
                    "Contract was saved successfully.\n" +
                    "New version: " + loadedContractVersion;

            loadData();

        } catch (OptimisticLockException exception) {
            contractDAO.clear();

            try {
                contractUpdateService.reloadAndSaveAfterOptimisticLockException(
                        loadedContractId,
                        editedMembershipType
                );

                optimisticLockDemoMessage =
                        "OptimisticLockException was caught.\n\n" +
                        "The form tried to save stale version: " + loadedContractVersion + ".\n\n" +
                        "Exception type:\n" +
                        exception.getClass().getName() + "\n\n" +
                        "Exception message:\n" +
                        exception.getMessage() + "\n\n" +
                        "A new transaction was started after the exception.\n" +
                        "The latest contract was reloaded and membership type was saved anyway.";

                loadData();
                clearEditingForm();

            } catch (RuntimeException recoveryException) {
                optimisticLockDemoMessage =
                        "OptimisticLockException was caught, but recovery save failed.\n\n" +
                        "Original exception type:\n" +
                        exception.getClass().getName() + "\n\n" +
                        "Original exception message:\n" +
                        exception.getMessage() + "\n\n" +
                        "Recovery exception type:\n" +
                        recoveryException.getClass().getName() + "\n\n" +
                        "Recovery exception message:\n" +
                        recoveryException.getMessage();

                clearEditingForm();
            }
        }

        return null;
    }

    public void refresh() {
        loadData();
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
