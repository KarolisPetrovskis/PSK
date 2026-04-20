package usecases;

import entities.Client;
import entities.Gym;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import lombok.Getter;
import persistance.ClientDAO;
import persistance.GymDAO;

import java.util.List;

@Model
public class Overview {

    @Inject
    private GymDAO gymDAO;

    @Inject
    private ClientDAO clientDAO;

    @Getter
    private List<Gym> allGymsWithTrainers;

    @Getter
    private List<Client> allClientsWithTrainers;

    @PostConstruct
    public void init() {
        allGymsWithTrainers = gymDAO.loadAllWithTrainers();
        allClientsWithTrainers = clientDAO.loadAllWithTrainers();
    }
}