package exceptions;

public class EmptyStudentListException extends Exception {
    public EmptyStudentListException() {
        super();
    }

    public EmptyStudentListException(String message) {
        super(message);
    }

}
