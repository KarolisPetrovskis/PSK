package usecases.extensibility.decorator;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BasicMessageService implements MessageService {

    @Override
    public String getMessage() {
        return "Decorator demo message";
    }
}
