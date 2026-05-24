package usecases.extensibility.decorator;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

@Decorator
public abstract class ExclamationMessageDecorator implements MessageService {

    @Inject
    @Delegate
    @Any
    private MessageService delegate;

    @Override
    public String getMessage() {
        return delegate.getMessage() + " decorated by CDI Decorator!";
    }
}
