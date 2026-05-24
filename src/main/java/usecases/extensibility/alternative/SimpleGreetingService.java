package usecases.extensibility.alternative;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Hello, " + name;
    }
}