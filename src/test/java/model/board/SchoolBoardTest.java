package model.board;


import exceptions.*;
import model.Player;
import org.junit.jupiter.api.AfterEach;
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
    SchoolBoard schoolBoard;

    @BeforeEach
    public void setUp() {
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
        assertEquals(false, schoolBoard.hasProfessor(Color.GREEN));
        //System.out.println("professors: " + schoolBoard.getProfessors().toString());
        assertEquals(0, schoolBoard.getProfessors().size());
    }

    @Test
    public void testTowers() {
        schoolBoard.setUpTowers(TowerColor.BLACK, 2);
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
    }
}
