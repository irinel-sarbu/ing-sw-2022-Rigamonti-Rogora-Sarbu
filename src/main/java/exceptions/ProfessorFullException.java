package exceptions;

public class ProfessorFullException extends Exception {
    public ProfessorFullException() {
        super();
    }

    public ProfessorFullException(String message) {
        super(message);
    }
}