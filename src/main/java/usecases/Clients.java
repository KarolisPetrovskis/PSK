package usecases;

import entities.Client;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import persistance.ClientDAO;

import java.util.List;

@Model
public class Clients {

    @Inject
    private ClientDAO clientDAO;

    @Getter @Setter
    private Client clientToCreate = new Client();

    @Getter
    private List<Client> allClients;

    @PostConstruct
    public void init() {
        loadAllClients();
    }

    @Transactional
    public String createClient() {
        this.clientDAO.persist(clientToCreate);
        return "clients?faces-redirect=true";
    }

    private void loadAllClients() {
        this.allClients = clientDAO.loadAll();
    }
}