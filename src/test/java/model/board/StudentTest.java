package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Color;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentTest {
    Student student1, student2;

    @BeforeEach
    public void setup() {
        student1 = new Student(0, Color.RED);
        student2 = new Student(1, Color.BLUE);
    }

    @Test
    public void getStudent() {
        assertTrue(student1.getID() == 0);
        assertTrue(student2.getID() == 1);
        assertTrue(student1.getColor() == Color.RED);
        assertTrue(student2.getColor() == Color.BLUE);
    }
}
