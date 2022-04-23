package model.board;

import util.Color;
import util.TowerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class IslandTile implements Comparable<IslandTile>, Serializable {
    private final List<Student> students;
    private Tower tower;
    private final int islandID;
    private boolean hasTower;

    //Since at the start of the game there is only 1 tile per islandGroup, they have the same ID

    /**
     * Class Constructor
     *
     * @param islandID specify the {@link IslandTile#islandID}
     */
    public IslandTile(int islandID) {
        students = new ArrayList<>();
        tower = null;
        this.islandID = islandID;
        this.hasTower = false;
    }

    /**
     * Get the ID of the current island
     *
     * @return the ID of the current island as an integer
     */
    public int getIslandID() {
        return islandID;
    }

    /**
     * Check if the current island has a tower on it
     *
     * @return true if island has a tower on it, false otherwise
     */
    public boolean getHasTower() {
        return hasTower;
    }

    /**
     * get the current tower color
     *
     * @return the tower color of the tower on this island, null if there is no tower
     */
    public TowerColor getTowerColor() {
        if (this.tower == null) return null;
        return tower.getColor();
    }

    /**
     * Set the tower color of the current island
     *
     * @param towerColor specify the tower color to set for this island
     */
    public void setTowerColor(TowerColor towerColor) {
        this.hasTower = towerColor != null;
        this.tower = new Tower(towerColor);
    }

    /**
     * Add a student to the current island
     *
     * @param student a reference to the student to add
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Get number of student of the specified color on the current island
     *
     * @param color specify students of which {@link Color} count on the island
     * @return the number of student of the specified color on the current island
     */
    public int getStudentsNumber(Color color) {
        return (int) students.stream()
                .map(Student::getColor)
                .filter(color::equals)
                .count();
    }

    /**
     * Tells which color is the most frequent on the island
     *
     * @return a {@link Color} representing the student color most frequent on the island
     */
    @Deprecated
    public Color getMostNumerous() {
        return students.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }

    /**
     * Compare two island tiles by their ID
     *
     * @param other the other island to compare to the current
     * @return an integer representing the order of the island tiles by their ID using the "compareTo" convention
     */
    public int compareTo(IslandTile other) {
        return Integer.compare(this.getIslandID(), other.getIslandID());
    }

    /**
     * Generate a string containing infos about the current island tile
     *
     * @return All info on the current island tile, including number of students grouped by color
     */
    @Override
    public String toString() {
        String stringID = String.format("%2s", islandID).replace(' ', '0');
        String stringContent = students.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream()
                .map(map -> map.getKey().toString() + ":" + String.format("%2s", map.getValue().toString()))
                .collect(Collectors.joining(" ", "[", "]"));
        return "Island_" + stringID + ": " + stringContent + (tower != null ? tower.toString() : "X");
    }
}
