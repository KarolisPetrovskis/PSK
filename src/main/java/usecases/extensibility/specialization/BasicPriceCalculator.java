package usecases.extensibility.specialization;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BasicPriceCalculator {

    public String calculatePrice() {
        return "Basic price calculator: monthly price is 30 EUR.";
    }
}
