package exceptions;

public class EmptyStudentListException extends RuntimeException {
    public EmptyStudentListException() {
        super();
    }

    public EmptyStudentListException(String message) {
        super(message);
    }

}
