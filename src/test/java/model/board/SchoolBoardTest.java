package model.board;


import exceptions.*;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Color;
import util.GameMode;
import util.TowerColor;
import util.Wizard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SchoolBoardTest {
    Player marco;
    SchoolBoard schoolBoard, normalSchoolboard;

    @BeforeEach
    public void setup() {
        marco = new Player("marco", Wizard.WIZARD_2, TowerColor.WHITE, GameMode.EXPERT);
        schoolBoard = new SchoolBoard(marco);
        for (int i = 0; i < 7; i++) {
            try {
                schoolBoard.addToEntrance(new Student(i, Color.BLUE));
            } catch (EntranceFullException e) {
                fail();
            }
        }
    }
    //Method addToEntrance tested in @BeforeEach

    @Test
    public void getEntrance() {
        assertEquals(7, schoolBoard.getEntranceStudents().size());
        try {
            assertSame(schoolBoard.getEntranceStudent(0).getColor(), Color.BLUE);
            assertNotNull(schoolBoard.getEntranceStudent(0));
        } catch (StudentNotFoundException e) {
            fail();
        }
        assertTrue(schoolBoard.toString().length() != 0);

        for (int i = 8; i < 10; i++) {
            try {
                schoolBoard.addToEntrance(new Student(i, Color.BLUE));
            } catch (EntranceFullException e) {
                fail();
            }
        }
        assertThrows(EntranceFullException.class, () -> schoolBoard.addToEntrance(new Student(11, Color.BLUE)));
    }

    @Test
    public void removeFromEntrance() {
        Student student = new Student(100, Color.RED);
        try {
            student = schoolBoard.removeFromEntrance(0);
        } catch (StudentNotFoundException e) {
            fail();
        }
        assertEquals(0, student.getID());
        assertSame(student.getColor(), Color.BLUE);
        assertThrows(StudentNotFoundException.class, () -> schoolBoard.removeFromEntrance(0));
        assertThrows(StudentNotFoundException.class, () -> schoolBoard.getEntranceStudent(0));
    }

    @Test
    public void testDining() {
        try {
            schoolBoard.addToDiningRoom(schoolBoard.removeFromEntrance(0));
            schoolBoard.addToDiningRoom(schoolBoard.removeFromEntrance(1));
            assertTrue(schoolBoard.addToDiningRoom(schoolBoard.removeFromEntrance(2)));
        } catch (DiningRoomFullException | StudentNotFoundException e) {
            fail();
        }
        assertEquals(3, schoolBoard.getStudentsOfColor(Color.BLUE));
        assertEquals(0, schoolBoard.getStudentsOfColor(Color.RED));
        try {
            schoolBoard.addToEntrance(schoolBoard.removeFromDiningRoom(Color.BLUE));
        } catch (DiningRoomEmptyException | EntranceFullException e) {
            fail();
        }
        assertEquals(5, schoolBoard.getEntranceStudents().size());
        assertEquals(2, schoolBoard.getStudentsOfColor(Color.BLUE));

        for (int i = 0; i < 10; i++) {
            try {
                schoolBoard.addToDiningRoom(new Student(i, Color.YELLOW));
            } catch (DiningRoomFullException e) {
                fail();
            }
        }
        assertThrows(DiningRoomFullException.class, () -> schoolBoard.addToDiningRoom(new Student(10, Color.YELLOW)));
        assertThrows(DiningRoomEmptyException.class, () -> schoolBoard.removeFromDiningRoom(Color.PINK));
    }

    @Test
    public void testProfessor() {
        Professor professor = new Professor(Color.GREEN);
        assertEquals(0, schoolBoard.getProfessors().size());
        try {
            schoolBoard.addProfessor(new Professor(Color.BLUE));
        } catch (ProfessorFullException e) {
            fail();
        }
        assertTrue(schoolBoard.hasProfessor(Color.BLUE));
        assertEquals(1, schoolBoard.getProfessors().size());
        try {
            schoolBoard.addProfessor(professor);
        } catch (ProfessorFullException e) {
            fail();
        }
        assertEquals(2, schoolBoard.getProfessors().size());
        assertTrue(schoolBoard.hasProfessor(Color.BLUE));
        try {
            schoolBoard.removeProfessor(professor);
            assertSame(schoolBoard.removeProfessorByColor(Color.BLUE).getColor(), Color.BLUE);
        } catch (ProfessorNotFoundException e) {
            fail();
        }
        assertFalse(schoolBoard.hasProfessor(Color.GREEN));
        //System.out.println("professors: " + schoolBoard.getProfessors().toString());
        assertEquals(0, schoolBoard.getProfessors().size());

        assertThrows(ProfessorNotFoundException.class, () -> schoolBoard.removeProfessor(new Professor(Color.PINK)));
        assertNull(schoolBoard.removeProfessorByColor(Color.PINK));
    }

    @Test
    public void testTowers() {
        schoolBoard.setupTowers(TowerColor.BLACK, 2);
        assertEquals(8, schoolBoard.getTowers().size());
        try {
            schoolBoard.removeTower();
        } catch (TowersIsEmptyException e) {
            fail();
        }
        assertEquals(7, schoolBoard.getTowers().size());
        try {
            schoolBoard.addTower(new Tower(TowerColor.BLACK));
        } catch (TowersFullException e) {
            fail();
        }
        assertEquals(8, schoolBoard.getTowers().size());
        assertThrows(TowersFullException.class, () -> schoolBoard.addTower(new Tower(TowerColor.BLACK)));
        System.out.println("------------->Trying to setup already set up towers");
        schoolBoard.setupTowers(TowerColor.BLACK, 2);
        for (int i = 0; i < 8; i++) {
            try {
                schoolBoard.removeTower();
            } catch (TowersIsEmptyException e) {
                fail();
            }
        }
        assertThrows(TowersIsEmptyException.class, () -> schoolBoard.removeTower());
    }

    @Test
    public void TestNormal() {
        marco = new Player("marco", Wizard.WIZARD_2, TowerColor.WHITE, GameMode.NORMAL);
        normalSchoolboard = new SchoolBoard(marco);
        for (int i = 0; i < 7; i++) {
            try {
                normalSchoolboard.addToEntrance(new Student(i, Color.BLUE));
            } catch (EntranceFullException e) {
                fail();
            }
        }
        assertTrue(normalSchoolboard.toString().length() != 0);
    }

    @Test
    public void print() {
        List<SchoolBoard> schoolBoardList = new ArrayList<>();
        try {
            schoolBoard.addToDiningRoom(new Student(10, Color.YELLOW));
            schoolBoard.addProfessor(new Professor(Color.YELLOW));
            schoolBoard.addTower(new Tower(TowerColor.WHITE));
            schoolBoard.addTower(new Tower(TowerColor.WHITE));

        } catch (DiningRoomFullException | ProfessorFullException | TowersFullException e) {
            fail();
        }

        schoolBoardList.add(schoolBoard);
        System.out.println(SchoolBoard.allToString(schoolBoardList));
        System.out.println(schoolBoard);
    }

    @Test
    public void printNormal() {
        marco = new Player("marco", Wizard.WIZARD_2, TowerColor.WHITE, GameMode.NORMAL);
        normalSchoolboard = new SchoolBoard(marco);

        List<SchoolBoard> normalSchoolboard = new ArrayList<>();
        normalSchoolboard.add(schoolBoard);
        System.out.println(SchoolBoard.allToString(normalSchoolboard));
        System.out.println(schoolBoard);
    }
}
