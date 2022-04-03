package exceptions;

public class LengthMismatchException extends RuntimeException {
    public LengthMismatchException() {
        super();
    }

    public LengthMismatchException(String message) {
        super(message);
    }
}