package exceptions;

public class EmptyNoEntryListException extends Exception {
    public EmptyNoEntryListException() {
        super();
    }

    public EmptyNoEntryListException(String message) {
        super(message);
    }
}
