package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

public class EStudentMovementToDining extends NetworkEvent {
    private final int studentID;

    public EStudentMovementToDining(int studentID) {
        super();
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
