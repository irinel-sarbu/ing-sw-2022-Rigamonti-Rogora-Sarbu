package model.board;

import java.util.*;
import java.util.stream.Collectors;

import util.TowerColor;
import util.Color;

public class IslandTile implements Comparable<IslandTile> {
    private List<Student> students;
    private Tower tower;
    private Integer islandID;
    private static int count = 0;

    public IslandTile() {
        students = new ArrayList<>();
        tower = null;
        this.islandID = count;
        count++;
    }

    public int getIslandID() {
        return islandID;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.tower = new Tower(towerColor);
    }

    public TowerColor getTowerColor() {
        return tower.getColor();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public int getStudentsNumber(Color color) {
        return (int) students.stream()
                .filter(s -> s.getColor().equals(color))
                .count();
    }

    public int compareTo(IslandTile other) {
        return Integer.compare(this.getIslandID(), other.getIslandID());
    }

    @Override
    public String toString() {
        String stringID = String.format("%2s", islandID.toString());
        String stringContent = students.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream()
                .map(map -> map.getKey().toString() + ":" + String.format("%2s", map.getValue().toString()))
                .collect(Collectors.joining(" ", "[", "]"));
        return "Island" + stringID + stringContent + (tower != null ? tower.toString() : "X");
    }
}
