package usecases;

import entities.Client;
import entities.Gym;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import lombok.Getter;
import mybatis.ClientMapper;
import mybatis.GymMapper;

import java.util.List;

@Model
public class OverviewMyBatis {

    @Inject
    private GymMapper gymMapper;

    @Inject
    private ClientMapper clientMapper;

    @Getter
    private List<Gym> allGymsWithTrainers;

    @Getter
    private List<Client> allClientsWithTrainers;

    @PostConstruct
    public void init() {
        allGymsWithTrainers = gymMapper.selectAllWithTrainers();
        allClientsWithTrainers = clientMapper.selectAllWithTrainers();
    }
}