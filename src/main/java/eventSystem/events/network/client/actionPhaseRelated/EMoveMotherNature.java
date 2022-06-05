package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

/**
 * Class that represents a client to server message.
 */
public class EMoveMotherNature extends NetworkEvent {
    private final int steps;

    /**
     * Default constructor
     *
     * @param steps how many steps mother nature should move
     */
    public EMoveMotherNature(int steps) {
        this.steps = steps;
    }

    /**
     * Getter
     *
     * @return steps mother nature should move
     */
    public int getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "EMoveMotherNature { steps: " + getSteps() + " }";
    }
}
