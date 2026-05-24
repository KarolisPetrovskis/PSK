package usecases;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import usecases.async.CalculationJob;
import usecases.async.CalculationJobRegistry;
import usecases.async.LongCalculationService;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@Named
@SessionScoped
public class AsyncCalculations implements Serializable {

    @Inject
    private LongCalculationService longCalculationService;

    @Inject
    private CalculationJobRegistry calculationJobRegistry;

    @Getter @Setter
    private int steps = 10;

    @Getter @Setter
    private int sleepMillis = 1000;

    @Getter
    private String currentJobId;

    @Getter
    private String message;

    @PostConstruct
    public void init() {
        message = "No calculation started yet.";
    }

    public String startCalculation() {
        String jobId = UUID.randomUUID().toString();

        CalculationJob job = new CalculationJob(jobId);
        calculationJobRegistry.put(job);

        currentJobId = jobId;
        message = "Calculation started asynchronously. Job id: " + jobId;

        longCalculationService.calculateAsync(jobId, steps, sleepMillis);

        return null;
    }

    public CalculationJob getCurrentJob() {
        if (currentJobId == null) {
            return null;
        }

        return calculationJobRegistry.get(currentJobId);
    }

    public Collection<CalculationJob> getAllJobs() {
        return calculationJobRegistry.getAll();
    }

    public String refresh() {
        return null;
    }
}
