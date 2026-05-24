package usecases.extensibility.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Logged
@Interceptor
public class LoggingInterceptor {

    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        System.out.println("CDI interceptor before method: " + context.getMethod().getName());

        try {
            Object result = context.proceed();

            System.out.println("CDI interceptor after method: " + context.getMethod().getName());

            return result;
        } catch (Exception exception) {
            System.out.println("CDI interceptor caught exception in method: " + context.getMethod().getName());
            throw exception;
        }
    }
}
