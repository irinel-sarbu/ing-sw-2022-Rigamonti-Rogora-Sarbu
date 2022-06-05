package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

/**
 * Class that represents a client to server message.
 */
public class ESelectRefillCloud extends NetworkEvent {
    private final int cloudID;

    /**
     * Default constructor
     *
     * @param cloudID selected cloud where to refill
     */
    public ESelectRefillCloud(int cloudID) {
        this.cloudID = cloudID;
    }

    /**
     * Getter
     *
     * @return selected cloud
     */
    public int getCloudID() {
        return cloudID;
    }

    @Override
    public String toString() {
        return "ESelectRefillCloud { cloudID=" + cloudID + " }";
    }
}
