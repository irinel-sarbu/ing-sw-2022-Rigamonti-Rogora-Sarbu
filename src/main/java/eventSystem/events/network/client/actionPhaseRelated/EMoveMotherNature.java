package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

public class EMoveMotherNature extends NetworkEvent {
    private final int steps;

    public EMoveMotherNature(int steps) {
        super();
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "EMoveMotherNature { steps: " + getSteps() + " }";
    }
}
