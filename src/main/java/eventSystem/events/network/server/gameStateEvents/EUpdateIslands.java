package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.IslandGroup;

import java.util.List;

/**
 * Class that represents a server to client message.
 */
public class EUpdateIslands extends Event {
    private final List<IslandGroup> islandGroups;
    private final int motherNaturePos;

    /**
     * Default constructor
     *
     * @param islandGroups    updated island groups
     * @param motherNaturePos new mother nature position
     */
    public EUpdateIslands(List<IslandGroup> islandGroups, int motherNaturePos) {
        this.islandGroups = islandGroups;
        this.motherNaturePos = motherNaturePos;
    }

    /**
     * Getter
     *
     * @return list of island group
     */
    public List<IslandGroup> getIslandGroups() {
        return islandGroups;
    }

    /**
     * Getter
     *
     * @return mother nature position
     */
    public int getMotherNaturePos() {
        return motherNaturePos;
    }

    @Override
    public String toString() {
        return "EUpdateIslands { MotherNature_position: " + motherNaturePos + " }";
    }
}
