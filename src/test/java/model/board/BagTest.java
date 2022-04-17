package model.board;

import exceptions.EmptyStudentListException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Color;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BagTest {

    private static Bag defaultBag;
    private static Bag customBag;
    private static Bag emptyBag;

    @BeforeAll
    public static void Bag() {
        defaultBag = new Bag(5);
        customBag = new Bag(0, 1, 2, 3, 4);
        emptyBag = new Bag(0);
    }

    @Test
    public void getRemainingStudents() {
        assertEquals(5 * 5, defaultBag.getRemainingStudents());
        assertEquals(1 + 2 + 3 + 4, customBag.getRemainingStudents());
    }

    @Test
    public void isEmpty() {
        assertTrue(emptyBag.isEmpty());
        assertFalse(customBag.isEmpty());
    }

    @Test
    public void pull() {
        try {
            emptyBag.pull();
            System.out.println("Expected EmptyStudentListException");
            fail();
        } catch (EmptyStudentListException e) {
            //
        }

        List<Student> tmp = new ArrayList<>();
        try {
            for (int i = 0; i < 5 * 5; i++) {
                int size = defaultBag.getRemainingStudents();
                tmp.add(defaultBag.pull());
            }
        } catch (EmptyStudentListException e) {
            fail();
        }

        for (Color color : Color.values()) {
            assertEquals(5, (int) tmp.stream().map(Student::getColor).filter(color::equals).count());
        }
    }

    @AfterEach
    public void resetBag() {
        defaultBag = new Bag(5);
        customBag = new Bag(0, 1, 2, 3, 4);
    }
}
