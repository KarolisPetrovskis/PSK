package usecases;

import entities.Trainer;
import entities.Gym;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import persistance.TrainerDAO;
import persistance.GymDAO;

import java.util.Map;

@Model
public class GymTrainers {

    @Inject
    private GymDAO gymDAO;

    @Inject
    private TrainerDAO trainerDAO;

    @Getter @Setter
    private Gym gym;

    @Getter @Setter
    private Trainer trainerToCreate = new Trainer();

    @PostConstruct
    public void init() {
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String gymId = requestParamMap.get("gymId");
        if (gymId != null) {
            this.gym = gymDAO.findOne(Long.parseLong(gymId));
        }
    }

    @Transactional
    public String createTrainer() {
        if (!trainerDAO.findByNameAndGym(trainerToCreate.getName(), this.gym).isEmpty()) {
            return "trainers?faces-redirect=true&gymId=" + this.gym.getId() + "&error=duplicate";
        }
        trainerToCreate.setGym(this.gym);
        trainerDAO.persist(trainerToCreate);
        return "trainers?faces-redirect=true&gymId=" + this.gym.getId();
    }
}