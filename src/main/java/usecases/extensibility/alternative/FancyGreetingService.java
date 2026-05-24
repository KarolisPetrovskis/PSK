package usecases.extensibility.alternative;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

@Alternative
@ApplicationScoped
public class FancyGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Welcome to the extensible CDI demo, " + name + "!";
    }
}
