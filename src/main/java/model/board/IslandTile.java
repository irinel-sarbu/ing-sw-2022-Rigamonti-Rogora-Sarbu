package model.board;

import java.util.*;
import java.util.stream.Collectors;

import util.TowerColor;
import util.Color;

public class IslandTile implements Comparable<IslandTile> {
    private List<Student> students;
    private TowerColor towerColor;
    private Integer islandID;
    private static int count = 0;

    public IslandTile() {
        students = new ArrayList<>();
        towerColor = null;
        this.islandID = count;
        count ++;
    }

    public int getIslandID() {
        return islandID;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void addStudent(Color color) {
        students.add(new Student(color));
    }

    public int getStudentsNumber(Color color) {
        return (int) students.stream()
                .filter(s -> s.getColor().equals(color))
                .count();
    }

    public Color getMostNumerous() {
        return students.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }

    public int compareTo ( IslandTile other){
        return Integer.compare(this.getIslandID(), other.getIslandID());
    }
}
