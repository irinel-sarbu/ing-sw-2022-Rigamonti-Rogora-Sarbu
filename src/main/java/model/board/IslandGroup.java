package model.board;

import exceptions.*;
import model.Player;
import model.expert.NoEntryTile;
import util.Color;
import util.TowerColor;

import java.util.*;
import java.util.stream.Collectors;

public class IslandGroup {
    private int islandGroupID;
    private final List<IslandTile> islands;
    private final Stack<NoEntryTile> noEntry;

    /**
     * IslandGroup constructor, also instantiate an empty {@link IslandTile} inside the islandGroup
     *
     * @param islandGroupID specify {@link IslandGroup#islandGroupID} and create an empty {@link IslandTile} with the same ID
     */
    public IslandGroup(int islandGroupID) {
        this.islandGroupID = islandGroupID;
        this.islands = new ArrayList<>();
        this.islands.add(new IslandTile(islandGroupID));
        this.noEntry = new Stack<>();
    }

    /**
     * Copy constructor
     *
     * @param islandGroup specify source {@link IslandGroup} to copy
     */
    private IslandGroup(IslandGroup islandGroup) {
        this.islandGroupID = islandGroup.islandGroupID;
        this.islands = new ArrayList<>();
        this.islands.addAll(islandGroup.islands);
        this.noEntry = new Stack<>();
        noEntry.addAll(islandGroup.noEntry);
    }

    /**
     * Get islands inside this group
     *
     * @return a {@link List} of {@link IslandTile} containing all islands in this group
     */
    public List<IslandTile> getIslands() {
        return islands;
    }

    /**
     * Get the island tile with the specified ID
     *
     * @param id island tile ID to search
     * @return an {@link IslandTile} with the specified ID return if the group contains it, null otherwise
     */
    public IslandTile getIslandTileByID(int id) {
        for (IslandTile island : islands) {
            if (island.getIslandID() == id)
                return island;
        }

        return null;
    }

    /**
     * Get current island group ID
     *
     * @return {@link IslandGroup#islandGroupID}
     */
    public int getIslandGroupID() {
        return islandGroupID;
    }

    /**
     * Set current island group ID
     *
     * @param islandGroupID the ID to assign to {@link IslandGroup#islandGroupID}
     */
    public void setIslandGroupID(int islandGroupID) {
        this.islandGroupID = islandGroupID;
    }

    /**
     * Get all islands id inside this group
     *
     * @return a {@link List} containing IDs of all islands inside this group
     */
    public List<Integer> getIslandTilesID() {
        return this.islands.stream().map(IslandTile::getIslandID).collect(Collectors.toList());
    }

    /**
     * Add a NoEntryTile to the current islandGroup
     *
     * @param noEntryTile a reference to the {@link NoEntryTile} to add
     */
    public void addNoEntry(NoEntryTile noEntryTile) {
        noEntryTile.setPosition(islandGroupID);
        noEntry.push(noEntryTile);
    }

    /**
     * Remove a noEntryTile from the current island group
     *
     * @return the reference to the removed {@link NoEntryTile}
     * @throws EmptyNoEntryListException if no {@link NoEntryTile} is present on the island group
     */
    public NoEntryTile removeNoEntry() throws EmptyNoEntryListException {
        NoEntryTile noEntryTile;
        if (noEntry.size() == 0) throw new EmptyNoEntryListException();
        noEntryTile = noEntry.pop();
        noEntryTile.setPosition(-1);
        return noEntryTile;
    }

    /**
     * Get the number of noEntry tiles on the current group
     *
     * @return the number of noEntry tiles present on the group
     */
    public int getNoEntrySize() {
        return this.noEntry.size();
    }

    /**
     * Get the size of the group
     *
     * @return the number of islands present on the current gropup
     */
    public int getSize() {
        return this.islands.size();
    }

    /**
     * Get the color of towers on the current group
     *
     * @return a {@link TowerColor} representing the color of the towers currently of the islands of this group
     */
    public TowerColor getTowersColor() {
        return this.islands.get(0).getTowerColor();
    }

    /**
     * Set group towerColor
     *
     * @param towerColor specify the {@link TowerColor} to set to the island group
     */
    public void setTowersColor(TowerColor towerColor) {
        this.islands.forEach(island -> island.setTowerColor(towerColor));
    }

    /**
     * Get the number of students of the specified color on the current group
     *
     * @param color specify students of which {@link Color} count on the group
     * @return the number of students of the specified color on the group
     */
    public int getStudentsNumber(Color color) {
        return this.islands.stream()
                .map(island -> island.getStudentsNumber(color))
                .reduce(Integer::sum).orElse(0);
    }

    /**
     * Join two group in a bigger one if their islands match {@link TowerColor}
     *
     * @param other reference to the group to join to the current
     * @return a new {@link IslandGroup} containing all the instances of {@link IslandTile} of the previous two, with the same {@link TowerColor} as the previous groups and all the {@link NoEntryTile} of the previous two groups
     * @throws IllegalIslandGroupJoinException if the {@link TowerColor} of the current and the other group differs
     * @throws NullIslandGroupException        if even one between the current and the other group has a null {@link TowerColor}
     */
    public IslandGroup join(IslandGroup other) throws IllegalIslandGroupJoinException, NullIslandGroupException {
        if (other == null) throw new NullIslandGroupException();
        if (this.getTowersColor() == null || other.getTowersColor() == null || !this.getTowersColor().equals(other.getTowersColor()))
            throw new IllegalIslandGroupJoinException();

        IslandGroup newIslandGroup = new IslandGroup(this);
        newIslandGroup.islands.addAll(other.islands);


        while (other.noEntry.size() > 0)
            newIslandGroup.noEntry.push(other.noEntry.pop());

        Collections.sort(newIslandGroup.islands);

        return newIslandGroup;
    }

    /**
     * Generate a string containing info about the group
     *
     * @return a string containing the group ID and all islands inside the group with their relative content
     */
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
