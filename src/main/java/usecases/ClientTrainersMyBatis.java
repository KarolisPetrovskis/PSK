package usecases;

import mybatis.model.Client;
import mybatis.model.Trainer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import mybatis.ClientMapper;
import mybatis.TrainerMapper;

import java.util.List;
import java.util.Map;

@Model
public class ClientTrainersMyBatis {

    @Inject
    private ClientMapper clientMapper;

    @Inject
    private TrainerMapper trainerMapper;

    @Getter
    private Client client;

    @Getter
    private List<Trainer> allTrainers;

    @Getter @Setter
    private Long trainerIdToAdd;

    @PostConstruct
    public void init() {
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String clientId = requestParamMap.get("clientId");
        if (clientId != null) {
            Long id = Long.parseLong(clientId);
            this.client = clientMapper.selectByPrimaryKeyWithTrainers(id);
            this.allTrainers = trainerMapper.selectAll();
        }
    }

    @Transactional
    public String addTrainerToClient() {
        if (client == null || trainerIdToAdd == null) {
            return "/mybatis/clients?faces-redirect=true";
        }

        if (clientMapper.countClientTrainer(client.getId(), trainerIdToAdd) == 0) {
            clientMapper.insertClientTrainer(client.getId(), trainerIdToAdd);
        }

        return "/mybatis/clientTrainers?faces-redirect=true&clientId=" + client.getId();
    }

    @Transactional
    public String removeTrainerFromClient(Long trainerId) {
        if (client == null || trainerId == null) {
            return "/mybatis/clients?faces-redirect=true";
        }

        clientMapper.deleteClientTrainer(client.getId(), trainerId);
        return "/mybatis/clientTrainers?faces-redirect=true&clientId=" + client.getId();
    }
}