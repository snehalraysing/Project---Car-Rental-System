package exception;

public class LeaseNotFoundException extends Throwable {
    public LeaseNotFoundException(String message) {
        super(message);
    }
}
