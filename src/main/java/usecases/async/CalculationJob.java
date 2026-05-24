package usecases.async;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class CalculationJob implements Serializable {

    private String id;
    private CalculationStatus status;
    private Integer progress;
    private Long result;
    private String message;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public CalculationJob(String id) {
        this.id = id;
        this.status = CalculationStatus.RUNNING;
        this.progress = 0;
        this.message = "Job started";
        this.startedAt = LocalDateTime.now();
    }
}
