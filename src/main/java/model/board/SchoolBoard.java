package model.board;

import java.awt.color.ColorSpace;
import java.util.*;
import java.util.stream.Collectors;

import exceptions.*;
import util.Color;

public class SchoolBoard {
    private static int count = 0;
    private final List<Professor> professors;
    private final static int maxProfessorsSize = Color.values().length;
    private final static int maxEntranceSize = 10;
    private final static int maxDiningSize = 10;
    private final static int maxTowersSize = 10;
    private final List<Student> entrance;
    private int[] diningRoom; // yellow = 0, blue = 1, green = 2, red = 3, pink = 4;
    private int ID;
    private final List<Tower> towers;

    public SchoolBoard(int ID) {
        this.ID = ID;
        this.entrance = new ArrayList<>();
        this.diningRoom = new int[Color.values().length];
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();
    }

    public SchoolBoard() {
        this(count);
        count++;
    }

    public List<Student> getEntranceStudents() {
        return entrance;
    }

    public void addToEntrance(Student... students) throws EntranceFullException {
        for (Student student : students) {
            if (entrance.size() >= maxEntranceSize) throw new EntranceFullException();
            entrance.add(student);
        }
    }

    public void removeFromEntrance(int position) throws StudentNotFoundException {
        boolean success = entrance.remove(position) != null;
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
        if (professors.size() >= maxProfessorsSize) throw new ProfessorFullException();
        professors.add(professor);
    }

    public void removeProfessor(Professor professor) throws ProfessorNotFoundException {
        boolean success;
        success = professors.remove(professor);
        if (!success) throw new ProfessorNotFoundException();
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public void addTower(Tower tower) throws TowersFullException {
        if (towers.size() >= maxTowersSize) throw new TowersFullException();
        towers.add(tower);
    }

    public void removeTower() throws TowersIsEmptyException {
        boolean success = towers.remove(0) != null;
        if (!success) throw new TowersIsEmptyException();
    }

    public boolean hasProfessor(Color color) {
        for (Professor professor : professors) {
            if (professor.getColor().equals(color)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String entranceString = entrance.stream()
                .map(Student::toString)
                .collect(Collectors.joining(" ", "[", "]"));
        String towerString = String.valueOf(towers.size());
        StringBuilder diningRoomString = new StringBuilder();
        for (Color color : Color.values()) {
            char[] colorStudents = new char[maxDiningSize];
            Arrays.fill(colorStudents, '-');
            for (int i = 2; i < maxDiningSize; i += 3) colorStudents[i] = 'o';
            for (int i = 0; i < diningRoom[color.getValue()]; i++) colorStudents[i] = color.toString().charAt(0);
            String colorProfessor = (this.hasProfessor(color) ? " X " : "   ");
            diningRoomString.append("\n\t").append(new String(colorStudents)).append(colorProfessor);
        }
        return "SchoolBoard " + ID + " entrance:" + entranceString + diningRoomString + " Towers:" + towerString;
    }
}
