package exceptions;

public class IslandGroupNotFoundException extends RuntimeException {
    public IslandGroupNotFoundException() {
        super();
    }

    public IslandGroupNotFoundException(String message) {
        super(message);
    }
}