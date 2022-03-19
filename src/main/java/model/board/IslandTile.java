package model.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.TowerColor;
import util.Color;

public class IslandTile {
    private List<Student> students;
    private TowerColor towerColor;
    private Integer islandID;

    public IslandTile(int islandID) {
        students = new ArrayList<>();
        towerColor = null;
        this.islandID = islandID;
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
        return (int) students.stream() // TODO: controlla se serve un Optional
                .filter(s -> s.getColor().equals(color))
                .count();
    }

    public Color getMostNumerous() {
        return students.stream() // TODO: devo imparare la prog. func.
                .filter(x -> x != null)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }
}
