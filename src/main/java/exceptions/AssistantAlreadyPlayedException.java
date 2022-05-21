package exceptions;

public class AssistantAlreadyPlayedException extends Exception {
    public AssistantAlreadyPlayedException() {
        super();
    }

    public AssistantAlreadyPlayedException(String message) {
        super(message);
    }
}