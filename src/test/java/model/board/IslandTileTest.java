package model.board;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Color;
import util.TowerColor;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTileTest {

    private static List<Student> students;

    private static IslandTile emptyIslandTile;
    private static IslandTile towerIslandTile;

    @BeforeAll
    public static void IslandTile() {
        students = new ArrayList<>();
        emptyIslandTile = new IslandTile(1);
        towerIslandTile = new IslandTile(2);
        towerIslandTile.setTowerColor(TowerColor.BLACK);

        for (int i = 0; i < Color.values().length * 2; i++) {
            students.add(new Student(i, Color.values()[i % Color.values().length]));
            towerIslandTile.addStudent(students.get(i));
        }
    }

    @Test
    public void getIslandID() {
        assertEquals(1, emptyIslandTile.getIslandID());
        assertEquals(2, towerIslandTile.getIslandID());
    }

    @Test
    public void getHasTower() {
        assertFalse(emptyIslandTile.getHasTower());
        assertTrue(towerIslandTile.getHasTower());
    }

    @Test
    public void getTowerColor() {
        assertNull(emptyIslandTile.getTowerColor());
        assertEquals(towerIslandTile.getTowerColor(), TowerColor.BLACK);
    }

    @Test
    public void getStudentsNumber() {
        for (Color color : Color.values()) {
            assertEquals(0, emptyIslandTile.getStudentsNumber(color));
            assertEquals(2, towerIslandTile.getStudentsNumber(color));
        }
    }

    @Test
    public void compareTo() {
        assertEquals(0, towerIslandTile.compareTo(towerIslandTile));
        assertTrue(emptyIslandTile.compareTo(towerIslandTile) < 0);
    }

    @AfterEach
    public void resetIslandTile() {
        emptyIslandTile = new IslandTile(1);
        towerIslandTile = new IslandTile(2);
        towerIslandTile.setTowerColor(TowerColor.BLACK);

        for (int i = 0; i < Color.values().length * 2; i++) {
            towerIslandTile.addStudent(students.get(i));
        }
    }

    @Test
    public void addStudent() {
        for (int i = 0; i < Color.values().length; i++) {
            emptyIslandTile.addStudent(new Student(100 + 2 * i, Color.values()[i]));
            towerIslandTile.addStudent(new Student(101 + 2 * i, Color.values()[i]));
            assertEquals(1, emptyIslandTile.getStudentsNumber(Color.values()[i]));
            assertEquals(3, towerIslandTile.getStudentsNumber(Color.values()[i]));
        }
    }

    @Test
    public void setTowerColor() {
        emptyIslandTile.setTowerColor(TowerColor.WHITE);
        towerIslandTile.setTowerColor(TowerColor.GRAY);
        assertEquals(emptyIslandTile.getTowerColor(), TowerColor.WHITE);
        assertEquals(towerIslandTile.getTowerColor(), TowerColor.GRAY);
    }

}
