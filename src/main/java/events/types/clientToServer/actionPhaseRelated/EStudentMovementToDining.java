package events.types.clientToServer.actionPhaseRelated;

import events.Event;
import events.EventType;

public class EStudentMovementToDining extends Event {
    private final int studentID;

    public EStudentMovementToDining(int studentID) {
        super(EventType.STUDENT_MOVEMENT_TO_DINING);
        this.studentID = studentID;
    }

    public int getStudentID() {
        return studentID;
    }

    @Override
    public String toString() {
        return "EStudentMovementToDining { student: " + getStudentID() + " }";
    }
}
