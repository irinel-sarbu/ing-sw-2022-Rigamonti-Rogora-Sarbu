package exceptions;

public class supplyEmptyException extends Exception {
    public supplyEmptyException() {
        super();
    }
    public supplyEmptyException(String message) {
        super(message);
    }
}