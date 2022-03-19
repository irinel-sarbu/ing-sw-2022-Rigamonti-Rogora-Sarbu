package model.board;

import java.util.*;
import exceptions.*;
import util.Color;

public class SchoolBoard {
    private final int maxProfessors = 5;
    private final int maxEntrance = 10;
    private final int maxDining = 10;
    private final int maxTowers = 10;
    private final List<Student> entrance;
    private int[] diningRoom; // yellow = 0, blue = 1, green = 2, red = 3, pink = 4;
    private final List<Professor> professors;
    private final List<Tower> towers;

    public SchoolBoard() {
        this.entrance = new ArrayList<>();
        this.diningRoom = new int[5];
        for (int i = 0; i < 5; i++) diningRoom[i] = 0;
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();
    }

    public List<Student> getEntranceStudents() {
        return entrance;
    }

    public void addToEntrance(Student... students) throws EntranceFullException {
        boolean success = true;
        for (Student student : students) {
            if (entrance.size() >= maxEntrance) throw new EntranceFullException();
            success = entrance.add(student);
            if (!success) throw new EntranceFullException();
        }
    }

    public void removeFromEntrance(int position) throws StudentNotFoundException {
        boolean success = true;
        success = entrance.remove(position) != null;
        if (!success) throw new StudentNotFoundException();
    }

    public void addToDiningRoom(Student student) throws DiningRoomFullException {
        diningRoom[student.getColor().getValue()] += 1;

        if (diningRoom[student.getColor().getValue()] > maxDining) {
            diningRoom[student.getColor().getValue()] -= 1;
            throw new DiningRoomFullException();
        }
    }


    public void removeFromDiningRoom(Color color) throws DiningRoomEmptyException {
        diningRoom[color.getValue()] -= 1;

        if (diningRoom[color.getValue()] < 0) {
            diningRoom[color.getValue()] += 1;
            throw new DiningRoomEmptyException();
        }

    }

    public int getStudentsOfColor(Color color) {
        return diningRoom[color.getValue()];
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void addProfessor(Professor professor) throws ProfessorFullException {
        boolean success = true;
        if (professors.size() >= maxProfessors) throw new ProfessorFullException();
        success = professors.add(professor);
        if (!success) throw new ProfessorFullException();
    }

    public void removeProfessor(Professor professor) throws ProfessorNotFoundException{
        boolean success = true;
        success = professors.remove(professor);
        if (!success) throw new ProfessorNotFoundException();
    }
}
