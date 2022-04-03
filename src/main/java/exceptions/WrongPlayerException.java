package exceptions;

public class WrongPlayerException extends Exception {
    public WrongPlayerException() {
        super();
    }

    public WrongPlayerException(String message) {
        super(message);
    }
}
