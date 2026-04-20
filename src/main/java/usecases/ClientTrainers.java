package usecases;

import entities.Client;
import entities.Trainer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import persistance.ClientDAO;
import persistance.TrainerDAO;

import java.util.List;
import java.util.Map;

@Model
public class ClientTrainers {

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private TrainerDAO trainerDAO;

    @Getter @Setter
    private Client client;

    @Getter @Setter
    private List<Trainer> allTrainers;

    @Getter @Setter
    private Long trainerIdToAdd;

    @PostConstruct
    public void init() {
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String clientId = requestParamMap.get("clientId");
        if (clientId != null) {
            this.client = clientDAO.findOne(Long.parseLong(clientId));
            this.allTrainers = trainerDAO.loadAll();
        }
    }

    @Transactional
    public String addTrainerToClient() {
        Trainer trainer = trainerDAO.findOne(trainerIdToAdd);
        if (!client.getTrainers().contains(trainer)) {
            client.getTrainers().add(trainer);
            clientDAO.update(client);
        }
        return "clientTrainers?faces-redirect=true&clientId=" + this.client.getId();
    }

    @Transactional
    public String removeTrainerFromClient(Long trainerId) {
        if (client == null || trainerId == null) {
            return "clients?faces-redirect=true";
        }

        Trainer trainer = trainerDAO.findOne(trainerId);
        if (trainer != null && client.getTrainers().contains(trainer)) {
            client.getTrainers().remove(trainer);
            clientDAO.update(client);
        }

        return "clientTrainers?faces-redirect=true&clientId=" + this.client.getId();
    }
}