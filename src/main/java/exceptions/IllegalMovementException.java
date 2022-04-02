package exceptions;

public class IllegalMovementException extends RuntimeException {
    public IllegalMovementException() {
        super();
    }

    public IllegalMovementException(String message) {
        super(message);
    }
}

