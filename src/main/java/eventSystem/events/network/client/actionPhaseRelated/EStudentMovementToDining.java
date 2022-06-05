package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

/**
 * Class that represents a client to server message.
 */
public class EStudentMovementToDining extends NetworkEvent {
    private final int studentID;

    /**
     * Default constructor
     *
     * @param studentID student to be moved to dining room
     */
    public EStudentMovementToDining(int studentID) {
        super();
        this.studentID = studentID;
    }

    /**
     * Getter
     *
     * @return student to be moved
     */
    public int getStudentID() {
        return studentID;
    }

    @Override
    public String toString() {
        return "EStudentMovementToDining { student: " + getStudentID() + " }";
    }
}
