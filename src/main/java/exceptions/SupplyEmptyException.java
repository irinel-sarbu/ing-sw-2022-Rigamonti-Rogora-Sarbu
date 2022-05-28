package exceptions;

public class SupplyEmptyException extends Exception {
    public SupplyEmptyException() {
        super();
    }
    public SupplyEmptyException(String message) {
        super(message);
    }
}