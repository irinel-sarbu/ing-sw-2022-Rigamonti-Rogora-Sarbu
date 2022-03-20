package exceptions;

public class AssistantNotInDeckException extends Exception {
    public AssistantNotInDeckException() {
        super();
    }

    public AssistantNotInDeckException(String message) {
        super(message);
    }
}