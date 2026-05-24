package usecases.extensibility.interceptor;

import jakarta.enterprise.context.ApplicationScoped;

@Logged
@ApplicationScoped
public class InterceptedReportService {

    public String generateReport() {
        return "Report generated. Check server logs to see interceptor messages.";
    }
}
