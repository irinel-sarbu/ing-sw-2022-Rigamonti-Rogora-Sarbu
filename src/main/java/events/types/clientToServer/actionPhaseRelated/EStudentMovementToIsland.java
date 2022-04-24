package events.types.clientToServer.actionPhaseRelated;

import events.Event;
import events.EventType;

public class EStudentMovementToIsland extends Event {
    private final int studentID, islandID;

    public EStudentMovementToIsland(int studentID, int islandID) {
        super(EventType.STUDENT_MOVEMENT_TO_ISLAND);
        this.studentID = studentID;
        this.islandID = islandID;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getIslandID() {
        return islandID;
    }

    @Override
    public String toString() {
        return "EStudentMovementToIsland { student: " + getStudentID() + ", island: " + getIslandID() + " }";
    }
}
