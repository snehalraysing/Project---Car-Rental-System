package exception;

public class CarNotFoundException extends Throwable {
    public CarNotFoundException(String message) {
        super(message);
    }
}
