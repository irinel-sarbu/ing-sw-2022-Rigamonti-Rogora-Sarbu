package exceptions;

public class NotEnoughCoinsException extends Exception {

    public NotEnoughCoinsException(String message) {
        super(message);
    }

    public NotEnoughCoinsException() {
        super();
    }
}
