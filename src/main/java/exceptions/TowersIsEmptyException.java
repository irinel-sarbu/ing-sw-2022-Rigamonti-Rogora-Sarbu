package exceptions;

public class TowersIsEmptyException extends Exception {
    public TowersIsEmptyException() {
        super();
    }

    public TowersIsEmptyException(String message) {
        super(message);
    }
}