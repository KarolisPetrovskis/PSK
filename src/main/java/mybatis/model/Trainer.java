package mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Trainer {
    private Long id;
    private String name;
    private Gym gym;
    private List<Client> clients = new ArrayList<>();

    public Trainer() {}

    public Trainer(String name) {
        this.name = name;
    }
}