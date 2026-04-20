package usecases;

import mybatis.model.Gym;
import mybatis.model.Trainer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import mybatis.GymMapper;
import mybatis.TrainerMapper;

import java.util.List;
import java.util.Map;

@Model
public class GymTrainersMyBatis {

    @Inject
    private GymMapper gymMapper;

    @Inject
    private TrainerMapper trainerMapper;

    @Getter @Setter
    private Gym gym;

    @Getter @Setter
    private Trainer trainerToCreate = new Trainer();

    @Getter
    private List<Trainer> trainersForGym;  // Explicitly load trainers

    @PostConstruct
    public void init() {
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String gymId = requestParamMap.get("gymId");
        if (gymId != null) {
            Long id = Long.parseLong(gymId);
            this.gym = gymMapper.selectByPrimaryKey(id);
            loadTrainersForGym();  // Load trainers separately
        }
    }

    private void loadTrainersForGym() {
        if (gym != null) {
            this.trainersForGym = trainerMapper.findByGymId(gym.getId());
        } else {
            this.trainersForGym = List.of();
        }
    }

    @Transactional
    public String createTrainer() {
//        if (this.gym == null) {
//            return "/mybatis/gyms?faces-redirect=true&error=gymNotFound";
//        }
//
//        // Check for duplicate trainer name in the same gym
//        if (!trainerMapper.findByNameAndGym(trainerToCreate.getName(), this.gym.getId()).isEmpty()) {
//            return "/mybatis/trainers?faces-redirect=true&gymId=" + this.gym.getId() + "&error=duplicate";
//        }

        trainerToCreate.setGym(this.gym);
        trainerMapper.insert(trainerToCreate);

        // Reload trainers to display the new one
        loadTrainersForGym();

        // Clear the form
        trainerToCreate = new Trainer();

        return "/mybatis/trainers?faces-redirect=true&gymId=" + this.gym.getId();
    }
}