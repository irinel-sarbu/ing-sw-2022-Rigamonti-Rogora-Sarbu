package events.types.serverToClient.gameStateEvents;

import events.Event;
import events.EventType;
import model.board.CloudTile;

import java.util.List;

public class EUpdateCloudTiles extends Event {
    private final List<CloudTile> cloudTiles;

    public EUpdateCloudTiles(List<CloudTile> cloudTiles) {
        super(EventType.UPDATE_CLOUD_TILES);
        this.cloudTiles = cloudTiles;
    }

    public List<CloudTile> getCloudTiles() {
        return cloudTiles;
    }

    @Override
    public String toString() {
        return "EUpdateCloudTiles { CloudTiles: " + cloudTiles.toString() + " }";
    }
}
