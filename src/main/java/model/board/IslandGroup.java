package model.board;

import exceptions.*;
import model.expert.NoEntryTile;
import util.Color;
import util.TowerColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class IslandGroup {
    private final int islandGroupID;
    private final List<IslandTile> islands;
    private Stack<NoEntryTile> noEntry;

    public IslandGroup(int islandGroupID) {
        this.islandGroupID = islandGroupID;
        this.islands = new ArrayList<>();
        this.islands.add(new IslandTile());
        this.noEntry = new Stack<>();
    }

    private IslandGroup(IslandGroup islandGroup) {
        this.islandGroupID = islandGroup.islandGroupID;
        this.islands = new ArrayList<>();
        this.islands.addAll(islandGroup.islands);
        this.noEntry = new Stack<>();
    }

    public List<IslandTile> getIslands() {
        return islands;
    }

    public IslandTile getIslandTileByID(int id) {
        for (IslandTile island : islands) {
            if (island.getIslandID() == id)
                return island;
        }

        throw new IslandNotFoundException("Island with id " + id + " not found!");
    }

    public int getIslandGroupID() {
        return islandGroupID;
    }

    public List<Integer> getIslandTilesID() {
        return this.islands.stream().map(IslandTile::getIslandID).collect(Collectors.toList());
    }

    public void addNoEntry(NoEntryTile noEntryTile) {
        noEntry.push(noEntryTile);
        noEntryTile.setPosition(islandGroupID);
    }

    public NoEntryTile removeNoEntry() throws EmptyNoEntryListException {
        NoEntryTile noEntryTile;
        if (noEntry.size() == 0) throw new EmptyNoEntryListException();
        noEntryTile = noEntry.pop();
        noEntryTile.setPosition(-1);
        return noEntryTile;
    }

    public int getNoEntrySize() {
        return this.noEntry.size();
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

    @Override
    public String toString() {
        String header = "IslandGroup " + String.format("%2s", islandGroupID);
        StringBuilder body = new StringBuilder();
        for (IslandTile islandTile : islands) {
            body.append("\n\t").append(islandTile);
        }
        return header + body;
    }
}
