package usecases;

import entities.Gym;
import lombok.Getter;
import lombok.Setter;
import persistance.GymDAO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@Model
public class Gyms {

    @Inject
    private GymDAO gymDAO;

    @Getter @Setter
    private Gym gymToCreate = new Gym();

    @Getter
    private List<Gym> allGyms;

    @PostConstruct
    public void init() {
        loadAllGyms();
    }

    @Transactional
    public String createGym() {
        this.gymDAO.persist(gymToCreate);
        return "gyms?faces-redirect=true";
    }

    private void loadAllGyms() {
        this.allGyms = gymDAO.loadAll();
    }
}