package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.CloudTile;

import java.util.List;

/**
 * Class that represents a server to client message.
 */
public class EUpdateCloudTiles extends Event {
    private final List<CloudTile> cloudTiles;

    /**
     * Default constructor
     *
     * @param cloudTiles updated cloud tiles
     */
    public EUpdateCloudTiles(List<CloudTile> cloudTiles) {
        this.cloudTiles = cloudTiles;
    }

    /**
     * Getter
     *
     * @return list of cloud tiles
     */
    public List<CloudTile> getCloudTiles() {
        return cloudTiles;
    }

    @Override
    public String toString() {
        return "EUpdateCloudTiles { CloudTiles: " + cloudTiles.toString() + " }";
    }
}
