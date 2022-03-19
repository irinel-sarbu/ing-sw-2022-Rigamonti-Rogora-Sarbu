package model.board;

import java.util.*;

import exceptions.EntranceFullException;

public class SchoolBoard {
    private final List<Student> entrance;
    private int[] diningRoom; // yellow = 0, blue = 1, green = 2, red = 3, pink = 4;
    private final List<Professor> professors;

    public SchoolBoard() {
        this.entrance = new ArrayList<>();
        diningRoom = new int[5];
        professors = new ArrayList<>();
    }

    public List<Student> getEntranceStudents() {
        return entrance;
    }

    public void addToEntrance(Student... students) throws EntranceFullException {
        for (Student student : students) {
            boolean success = entrance.add(student);
            if (!success)
                throw new EntranceFullException();
        }
    }
}
