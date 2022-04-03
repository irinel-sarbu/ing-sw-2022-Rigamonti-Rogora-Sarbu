package exceptions;

public class CharacterCardNotFound extends Exception {
    public CharacterCardNotFound() {
        super();
    }

    public CharacterCardNotFound(String message) {
        super(message);
    }
}