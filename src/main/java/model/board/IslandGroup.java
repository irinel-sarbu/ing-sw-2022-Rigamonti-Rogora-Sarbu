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
    private boolean noEntry;

    public IslandGroup() {
        this.islands = new ArrayList<>();
        this.islands.add(new IslandTile());
        this.noEntry = false;
    }

    private IslandGroup(IslandGroup islandGroup) {
        this.islands = new ArrayList<>();
        this.islands.addAll(islandGroup.islands);
        this.noEntry = false;
    }

    public List<Integer> getIslandTilesID() {
        return this.islands.stream().map(IslandTile::getIslandID).collect(Collectors.toList());
    }

    public void toggleNoEntry() {
        this.noEntry = !this.noEntry;
    }

    public boolean getNoEntry() {
        return this.noEntry;
    }

    public int getSize() {
        return this.islands.size();
    }

    public void setTowersColor(TowerColor towerColor) throws NullTowerColorException {
        if (towerColor == null) throw new NullTowerColorException();
        this.islands.forEach(island -> island.setTowerColor(towerColor));
    }

    public TowerColor getTowersColor() {
        return this.islands.get(0).getTowerColor();
    }

    public int getStudentsNumber(Color color) {
        return this.islands.stream()
                .map(island -> island.getStudentsNumber(color))
                .reduce(Integer::sum).orElse(0);
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
