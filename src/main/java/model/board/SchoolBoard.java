package model.board;

import java.util.*;

import exceptions.*;
import util.Color;

public class SchoolBoard {
    private final int maxProfessorsSize = Color.values().length;
    private final int maxEntranceSize = 10;
    private final int maxDiningSize = 10;
    private final int maxTowersSize = 10;
    private final List<Student> entrance;
    private int[] diningRoom; // yellow = 0, blue = 1, green = 2, red = 3, pink = 4;
    private final List<Professor> professors;
    private final List<Tower> towers;

    public SchoolBoard() {
        this.entrance = new ArrayList<>();
        this.diningRoom = new int[Color.values().length];
        Arrays.fill(diningRoom, 0);
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();
    }

    public List<Student> getEntranceStudents() {
        return entrance;
    }

    public void addToEntrance(Student... students) throws EntranceFullException {
        boolean success = true;
        for (Student student : students) {
            if (entrance.size() >= maxEntranceSize) throw new EntranceFullException();
            success = entrance.add(student);
            if (!success) throw new EntranceFullException();
        }
    }

    public void removeFromEntrance(int position) throws StudentNotFoundException {
        boolean success = true;
        if (entrance.size() == 0) throw new StudentNotFoundException();
        success = entrance.remove(position) != null;
        if (!success) throw new StudentNotFoundException();
    }

    public void addToDiningRoom(Student student) throws DiningRoomFullException {
        diningRoom[student.getColor().getValue()] += 1;

        if (diningRoom[student.getColor().getValue()] > maxDiningSize) {
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
        if (professors.size() >= maxProfessorsSize) throw new ProfessorFullException();
        success = professors.add(professor);
        if (!success) throw new ProfessorFullException();
    }

    public void removeProfessor(Professor professor) throws ProfessorNotFoundException {
        boolean success = true;
        if (professors.size() == 0) throw new ProfessorNotFoundException();
        success = professors.remove(professor);
        if (!success) throw new ProfessorNotFoundException();
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public void addTower(Tower tower) throws TowersFullException {
        boolean success = true;
        if (towers.size() >= maxTowersSize) throw new TowersFullException();
        success = towers.add(tower);
        if (!success) throw new TowersFullException();
    }

    public void removeTower() throws TowersIsEmptyException {
        boolean success = true;
        if (towers.size() == 0) throw new TowersIsEmptyException();
        success = towers.remove(0) != null;
        if (!success) throw new TowersIsEmptyException();
    }

}
