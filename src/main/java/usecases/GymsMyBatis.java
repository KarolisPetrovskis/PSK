package usecases;

import entities.Gym;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import mybatis.GymMapper;

import java.util.List;

@Model
public class GymsMyBatis {

    @Inject
    private GymMapper gymMapper;

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
        gymMapper.insert(gymToCreate);
        return "/mybatis/gyms?faces-redirect=true";
    }

    private void loadAllGyms() {
        this.allGyms = gymMapper.selectAll();
    }
}