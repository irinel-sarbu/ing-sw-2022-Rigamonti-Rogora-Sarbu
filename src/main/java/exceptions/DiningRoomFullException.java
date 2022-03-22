package exceptions;

public class DiningRoomFullException extends Exception {
    public DiningRoomFullException() {
        super();
    }

    public DiningRoomFullException(String message) {
        super(message);
    }
}
