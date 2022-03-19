package exceptions;

public class TowerFullException extends Exception {
    public TowerFullException() {
        super();
    }

    public TowerFullException(String message) {
        super(message);
    }
}