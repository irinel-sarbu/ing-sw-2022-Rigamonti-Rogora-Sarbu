package exceptions;

public class IslandNotFoundException extends RuntimeException {
    public IslandNotFoundException() {
        super();
    }

    public IslandNotFoundException(String message) {
        super(message);
    }
}