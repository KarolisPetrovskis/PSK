package usecases.async;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CalculationJobRegistry {

    private final Map<String, CalculationJob> jobs = new ConcurrentHashMap<>();

    public void put(CalculationJob job) {
        jobs.put(job.getId(), job);
    }

    public CalculationJob get(String id) {
        return jobs.get(id);
    }

    public Collection<CalculationJob> getAll() {
        return jobs.values();
    }

    public void remove(String id) {
        jobs.remove(id);
    }
}
