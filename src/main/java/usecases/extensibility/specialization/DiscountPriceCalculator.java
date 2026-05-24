package usecases.extensibility.specialization;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Specializes;

@Specializes
@ApplicationScoped
public class DiscountPriceCalculator extends BasicPriceCalculator {

    @Override
    public String calculatePrice() {
        return "Specialized discount calculator: monthly price is 25 EUR.";
    }
}
