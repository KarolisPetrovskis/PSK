package usecases.async;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@Stateless
public class LongCalculationService {

    @Inject
    private CalculationJobRegistry calculationJobRegistry;

    @Asynchronous
    public void calculateAsync(String jobId, int steps, int sleepMillis) {
        CalculationJob job = calculationJobRegistry.get(jobId);

        if (job == null) {
            return;
        }

        try {
            long result = 0;

            for (int i = 1; i <= steps; i++) {
                Thread.sleep(sleepMillis);

                result += (long) i * i;

                int progress = (int) (((double) i / steps) * 100);
                job.setProgress(progress);
                job.setMessage("Completed step " + i + " of " + steps);
            }

            job.setResult(result);
            job.setProgress(100);
            job.setStatus(CalculationStatus.COMPLETED);
            job.setMessage("Calculation completed successfully");
            job.setFinishedAt(LocalDateTime.now());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            job.setStatus(CalculationStatus.FAILED);
            job.setMessage("Calculation was interrupted: " + exception.getMessage());
            job.setFinishedAt(LocalDateTime.now());
        } catch (Exception exception) {
            job.setStatus(CalculationStatus.FAILED);
            job.setMessage("Calculation failed: " + exception.getMessage());
            job.setFinishedAt(LocalDateTime.now());
        }
    }
}
