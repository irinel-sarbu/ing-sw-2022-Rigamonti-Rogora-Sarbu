package exceptions;

public class WrongPhaseException extends Exception {
    public WrongPhaseException() {
        super();
    }

    public WrongPhaseException(String message) {
        super(message);
    }
}
