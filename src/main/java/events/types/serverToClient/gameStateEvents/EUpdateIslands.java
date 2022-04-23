package events.types.serverToClient.gameStateEvents;

import events.Event;
import events.EventType;
import model.board.IslandGroup;

import java.util.List;

public class EUpdateIslands extends Event {
    private final List<IslandGroup> islandGroups;
    private final int motherNaturePos;

    public EUpdateIslands(List<IslandGroup> islandGroups, int motherNaturePos) {
        super(EventType.UPDATE_ISLANDS);
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
        return "EUpdateIslands { islandGroups: " + islandGroups.toString() + "\n MotherNature Position: " + motherNaturePos + " }";
    }
}
