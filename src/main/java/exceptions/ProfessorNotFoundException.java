package exceptions;

public class ProfessorNotFoundException extends Exception {
    public ProfessorNotFoundException() {
        super();
    }

    public ProfessorNotFoundException(String message) {
        super(message);
    }
}