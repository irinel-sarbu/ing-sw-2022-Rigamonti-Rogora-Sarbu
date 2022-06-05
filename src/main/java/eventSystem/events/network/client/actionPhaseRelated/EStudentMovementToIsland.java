package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

/**
 * Class that represents a client to server message.
 */
public class EStudentMovementToIsland extends NetworkEvent {
    private final int studentID, islandID;

    /**
     * Default constructor
     *
     * @param studentID student to be moved
     * @param islandID  island where student should be moved
     */
    public EStudentMovementToIsland(int studentID, int islandID) {
        this.studentID = studentID;
        this.islandID = islandID;
    }

    /**
     * Getter
     *
     * @return student to be moved
     */
    public int getStudentID() {
        return studentID;
    }

    /**
     * Getter
     *
     * @return island where student should be moved
     */
    public int getIslandID() {
        return islandID;
    }

    @Override
    public String toString() {
        return "EStudentMovementToIsland { student: " + getStudentID() + ", island: " + getIslandID() + " }";
    }
}
