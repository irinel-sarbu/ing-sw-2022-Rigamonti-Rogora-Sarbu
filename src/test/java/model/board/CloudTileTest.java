package model.board;

import exceptions.TooManyStudentsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Color;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CloudTileTest {

    private static CloudTile full;
    private static CloudTile half;
    private static CloudTile empty;

    @BeforeAll
    public static void setup() {
        full = new CloudTile(0, 4);
        try {
            full.put(
                    new Student(0, Color.RED),
                    new Student(1, Color.GREEN),
                    new Student(2, Color.BLUE),
                    new Student(3, Color.YELLOW));
        } catch (TooManyStudentsException e) {
            System.err.println("TooManyStudentsException not expected");
        }
        try {
            full.put(new Student(10, Color.RED));
            System.err.println("expected TooManyStudentsException");
            fail();
        } catch (TooManyStudentsException e) {
            //
        }
        half = new CloudTile(1, 4);
        try {
            half.put(
                    new Student(4, Color.RED),
                    new Student(5, Color.GREEN));
        } catch (TooManyStudentsException e) {
            e.printStackTrace();
        }
        empty = new CloudTile(2, 4);
    }

    @Test
    public void isEmpty() {
        assertTrue(empty.isEmpty());
        assertFalse(full.isEmpty());
    }

    @AfterEach
    public void resetCloudTile() {
        full = new CloudTile(0, 4);
        try {
            full.put(
                    new Student(0, Color.RED),
                    new Student(1, Color.GREEN),
                    new Student(2, Color.BLUE),
                    new Student(3, Color.YELLOW));
        } catch (TooManyStudentsException e) {
            System.err.println("TooManyStudentsException not expected");
            fail();
        }
        try {
            full.put(new Student(10, Color.RED));
            System.err.println("expected TooManyStudentsException");
            fail();
        } catch (TooManyStudentsException e) {
            //
        }
        half = new CloudTile(1, 4);
        try {
            half.put(
                    new Student(4, Color.RED),
                    new Student(5, Color.GREEN));
        } catch (TooManyStudentsException e) {
            e.printStackTrace();
        }
        empty = new CloudTile(2, 4);
    }

    @Test
    public void put() {
        try {
            full.put(new Student(100, Color.RED));
            System.err.println("expected TooManyStudentsException");
            fail();
        } catch (TooManyStudentsException e) {
            //
        }

        try {
            empty.put(new Student(100, Color.RED));
        } catch (TooManyStudentsException e) {
            System.err.println("TooManyStudentsException not expected");
            fail();
        }
    }

    @Test
    public void getStudents() {
        assertEquals(4, full.getStudents().size());
        assertEquals(0, empty.getStudents().size());
    }

    @Test
    public void getAndRemoveStudents() {
        assertTrue(full.toString().length() != 0);
        assertEquals(4, full.getAndRemoveStudents().size());
        assertEquals(0, full.getStudents().size());
    }

    @Test
    public void print() {
        Student s1 = new Student(0, Color.YELLOW);
        Student s2 = new Student(0, Color.BLUE);
        Student s3 = new Student(0, Color.GREEN);
        Student s4 = new Student(0, Color.RED);

        CloudTile c1 = new CloudTile(0, 3);
        CloudTile c2 = new CloudTile(1, 3);
        try {
            c2.put(s1, s2, s3);
        } catch (TooManyStudentsException e) {
            fail();
        }
        CloudTile c3 = new CloudTile(2, 4);
        try {
            c3.put(s1, s2, s3, s4);
        } catch (TooManyStudentsException e) {
            fail();
        }

        List<CloudTile> cloudTileList = new ArrayList<>();
        cloudTileList.add(c1);
        cloudTileList.add(c2);
        cloudTileList.add(c3);

        System.out.println(CloudTile.allToString(cloudTileList));
    }

}
