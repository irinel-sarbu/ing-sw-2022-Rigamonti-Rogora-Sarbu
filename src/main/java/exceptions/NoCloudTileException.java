package exceptions;

public class NoCloudTileException extends Exception {

    public NoCloudTileException(String message) {
        super(message);
    }

    public NoCloudTileException() {
        super();
    }
}
