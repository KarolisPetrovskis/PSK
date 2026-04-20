package mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Gym {
    private Long id;
    private String name;
    private List<Trainer> trainers = new ArrayList<>();

    public Gym() {}

    public Gym(String name) {
        this.name = name;
    }
}