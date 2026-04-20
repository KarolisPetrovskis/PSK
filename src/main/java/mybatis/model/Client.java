package mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Client {
    private Long id;
    private String name;
    private List<Trainer> trainers = new ArrayList<>();

    public Client() {}

    public Client(String name) {
        this.name = name;
    }
}