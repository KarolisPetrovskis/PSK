package usecases;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import usecases.extensibility.alternative.GreetingService;
import usecases.extensibility.decorator.MessageService;
import usecases.extensibility.interceptor.InterceptedReportService;
import usecases.extensibility.specialization.BasicPriceCalculator;

@Named
@RequestScoped
public class ExtensibilityDemo {

    @Inject
    private GreetingService greetingService;

    @Inject
    private BasicPriceCalculator priceCalculator;

    @Inject
    private InterceptedReportService interceptedReportService;

    @Inject
    private MessageService messageService;

    public String getAlternativeResult() {
        return greetingService.greet("student");
    }

    public String getSpecializationResult() {
        return priceCalculator.calculatePrice();
    }

    public String getInterceptorResult() {
        return interceptedReportService.generateReport();
    }

    public String getDecoratorResult() {
        return messageService.getMessage();
    }
}
