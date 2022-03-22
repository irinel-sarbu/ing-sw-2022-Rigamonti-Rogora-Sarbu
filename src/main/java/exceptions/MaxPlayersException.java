package exceptions;

public class MaxPlayersException extends Exception {

    public MaxPlayersException(String message) {
        super(message);
    }

    public MaxPlayersException() {
        super();
    }
}
