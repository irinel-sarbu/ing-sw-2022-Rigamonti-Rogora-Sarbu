package model.board;

import java.util.*;
import java.util.stream.Collectors;

import exceptions.TowersIsEmptyException;
import util.TowerColor;
import util.Color;

public class IslandTile implements Comparable<IslandTile> {
    private final List<Student> students;
    private Tower tower;
    private final int islandID;
    private boolean hasTower;

    //Since at the start of the game there is only 1 tile per islandGroup, they have the same ID
    public IslandTile(int islandID) {
        students = new ArrayList<>();
        tower = null;
        this.islandID = islandID;
        this.hasTower=false;
    }

    public int getIslandID() {
        return islandID;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.hasTower = true;
        this.tower = new Tower(towerColor);
    }

    public boolean getHasTower() {
        return hasTower;
    }

    public TowerColor getTowerColor() {
        if (this.tower == null) return null;
        return tower.getColor();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public int getStudentsNumber(Color color) {
        return (int) students.stream()
                .map(Student::getColor)
                .filter(color::equals)
                .count();
    }

    @Deprecated
    public Color getMostNumerous() {
        return students.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }

    public int compareTo(IslandTile other) {
        return Integer.compare(this.getIslandID(), other.getIslandID());
    }

    @Override
    public String toString() {
        String stringID = String.format("%2s", islandID);
        String stringContent = students.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream()
                .map(map -> map.getKey().toString() + ":" + String.format("%2s", map.getValue().toString()))
                .collect(Collectors.joining(" ", "[", "]"));
        return "Island" + stringID + stringContent + (tower != null ? tower.toString() : "X");
    }
}
