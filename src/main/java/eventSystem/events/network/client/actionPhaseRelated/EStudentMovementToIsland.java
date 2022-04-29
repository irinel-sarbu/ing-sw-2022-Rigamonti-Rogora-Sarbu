package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

public class EStudentMovementToIsland extends NetworkEvent {
    private final int studentID, islandID;

    public EStudentMovementToIsland(int studentID, int islandID) {
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
