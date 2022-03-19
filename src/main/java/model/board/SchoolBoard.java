package model.board;

import java.util.*;

import exceptions.DiningRoomFullException;
import exceptions.EntranceFullException;
import exceptions.StudentNotFoundException;

public class SchoolBoard {
    private final List<Student> entrance;
    private int[] diningRoom; // yellow = 0, blue = 1, green = 2, red = 3, pink = 4;
    private final List<Professor> professors;
    private final List<Tower> towers;

    public SchoolBoard() {
        this.entrance = new ArrayList<>();
        this.diningRoom = new int[5];
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();
    }

    public List<Student> getEntranceStudents() {
        return entrance;
    }

    public void addToEntrance(Student... students) throws EntranceFullException {
        boolean success = true;
        for (Student student : students) {
            success = entrance.add(student);
            if (!success)
                throw new EntranceFullException();
        }
    }

    public void removeFromEntrance(int position) throws StudentNotFoundException {
        boolean success = true;
        success = entrance.remove(position) != null;
        if (!success)
            throw new StudentNotFoundException();
    }

    public void addToDiningRoom(Student student) throws DiningRoomFullException {
        diningRoom[student.getColor().getValue()] += 1;

        if (diningRoom[student.getColor().getValue()] > 10) {
            diningRoom[student.getColor().getValue()] -= 1;
            throw new DiningRoomFullException();
        }
    }

}
