package usecases;

import mybatis.model.Client;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import mybatis.ClientMapper;

import java.util.List;

@Model
public class ClientsMyBatis {

    @Inject
    private ClientMapper clientMapper;

    @Getter @Setter
    private Client clientToCreate = new Client();

    @Getter
    private List<Client> allClients;

    @PostConstruct
    public void init() {
        loadAllClients();
    }

    @Transactional
    public String createClient() {
        clientMapper.insert(clientToCreate);
        return "/mybatis/clients?faces-redirect=true";
    }

    private void loadAllClients() {
        this.allClients = clientMapper.selectAll();
    }
}