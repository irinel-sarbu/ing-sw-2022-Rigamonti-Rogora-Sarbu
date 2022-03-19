package exceptions;

public class DiningRoomEmptyException extends Exception {
    public DiningRoomEmptyException() {
        super();
    }

    public DiningRoomEmptyException(String message) {
        super(message);
    }
}