package model.board;

import exceptions.IllegalIslandGroupJoinException;
import exceptions.NullIslandGroupException;
import exceptions.NullTowerColorException;
import util.Color;
import util.TowerColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IslandGroup {
    private final List<IslandTile> islands;

    public IslandGroup() {
        islands = new ArrayList<>();
        islands.add(new IslandTile());
    }

    public List<Integer> getIslandTilesID() {
        return islands.stream().map(IslandTile::getIslandID).collect(Collectors.toList());
    }

    public IslandGroup(IslandGroup islandGroup) {
        islands = new ArrayList<>();
        islands.addAll(islandGroup.islands);
    }

    public int getSize() {
        return islands.size();
    }

    public void setTowersColor(TowerColor towerColor) throws NullTowerColorException {
        if (towerColor == null) throw new NullTowerColorException();
        for (int i = 0; i < this.getSize(); i++) {
            islands.get(i).setTowerColor(towerColor);
        }
    }

    public TowerColor getTowersColor() {
        return islands.get(0).getTowerColor();
    }

    public int getStudentsNumber(Color color) {
        int total = 0;
        for (IslandTile island : islands) {
            total += island.getStudentsNumber(color);
        }
        return total;
    }

    public IslandGroup join(IslandGroup other) throws IllegalIslandGroupJoinException, NullIslandGroupException {
        if (other == null) throw new NullIslandGroupException();
        if (!this.getTowersColor().equals(other.getTowersColor())) throw new IllegalIslandGroupJoinException();

        IslandGroup newIslandGroup = new IslandGroup(this);
        newIslandGroup.islands.addAll(other.islands);
        Collections.sort(newIslandGroup.islands);

        return newIslandGroup;
    }

}
