package eventSystem.events.network.client.actionPhaseRelated;

import eventSystem.events.network.NetworkEvent;

public class ESelectRefillCloud extends NetworkEvent {
    private final int cloudID;

    public ESelectRefillCloud(int cloudID) {
        super();
        this.cloudID = cloudID;
    }

    public int getCloudID() {
        return cloudID;
    }

    @Override
    public String toString() {
        return "ESelectRefillCloud { cloudID=" + cloudID + " }";
    }
}
