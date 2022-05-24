package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.IslandGroup;

import java.util.List;

public class EUpdateIslands extends Event {
    private final List<IslandGroup> islandGroups;
    private final int motherNaturePos;

    public EUpdateIslands(List<IslandGroup> islandGroups, int motherNaturePos) {
        this.islandGroups = islandGroups;
        this.motherNaturePos = motherNaturePos;
    }

    public List<IslandGroup> getIslandGroups() {
        return islandGroups;
    }

    public int getMotherNaturePos() {
        return motherNaturePos;
    }

    @Override
    public String toString() {
        return "EUpdateIslands { MotherNature_position: " + motherNaturePos + " }";
    }
}
