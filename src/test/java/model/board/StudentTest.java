package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Color;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    Student student1, student2;

    @BeforeEach
    public void setup() {
        student1 = new Student(0, Color.RED);
        student2 = new Student(1, Color.BLUE);
    }

    @Test
    public void getStudent() {
        assertEquals(0, student1.getID());
        assertEquals(1, student2.getID());
        assertSame(student1.getColor(), Color.RED);
        assertSame(student2.getColor(), Color.BLUE);
        assertTrue(student2.toString().length() != 0);
    }
}
