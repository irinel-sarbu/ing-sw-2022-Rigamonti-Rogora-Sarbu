package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.CloudTile;

import java.util.List;

public class EUpdateCloudTiles extends Event {
    private final List<CloudTile> cloudTiles;

    public EUpdateCloudTiles(List<CloudTile> cloudTiles) {
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
