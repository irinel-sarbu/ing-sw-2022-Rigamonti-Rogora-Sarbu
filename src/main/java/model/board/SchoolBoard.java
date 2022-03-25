package model.board;

import java.awt.color.ColorSpace;
import java.util.*;
import java.util.stream.Collectors;

import exceptions.*;
import util.Color;

public class SchoolBoard {
    private static int count = 0;
    private final static int maxEntranceSize = 9;
    private int coins;
    private final List<Professor> professors;
    private final static int maxProfessorsSize = Color.values().length;
    private final static int maxTowersSize = 8;
    private final static int maxDiningSize = 10;
    private final int ID;
    private final List<Student> entrance;                                                                               // TODO: check if List must be final or not
    private final Stack<Student>[] diningRoom;
    private final List<Tower> towers;

    public SchoolBoard(int ID) {
        this.ID = ID;
        this.coins = 0;
        this.entrance = new ArrayList<>();
        this.diningRoom = new Stack[Color.values().length];
        for (int i = 0; i < maxProfessorsSize; i++) diningRoom[i] = new Stack<>();
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
        if (diningRoom[student.getColor().getValue()].size() + 1 > maxDiningSize) {
            throw new DiningRoomFullException();
        }
        diningRoom[student.getColor().getValue()].push(student);
        if ((diningRoom[student.getColor().getValue()].size() % 3) == 0) {
            addCoin();
        }
    }

    public Student removeFromDiningRoom(Color color) throws DiningRoomEmptyException {
        if (diningRoom[color.getValue()].size() == 0) {
            throw new DiningRoomEmptyException();
        }
        return diningRoom[color.getValue()].pop();
    }

    public int getStudentsOfColor(Color color) {
        return diningRoom[color.getValue()].size();
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

    public static String allToString(List<SchoolBoard> boards) {
        List<String[]> allBoards = boards.stream()
                .map(SchoolBoard::toString)
                .map(bs -> bs.split("\n"))
                .collect(Collectors.toList());
        StringBuilder boardsString = new StringBuilder();

        for (int i = 0; i < allBoards.get(0).length; i++) {
            for (String[] allBoard : allBoards) {
                boardsString.append(String.format("%-32s", allBoard[i]));
            }
            boardsString.append("\n");
        }
        return new String(boardsString);
    }

    private void addCoin() {
        coins += 1;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void removeCoins(int coins) throws NotEnoughCoinsException {
        if (coins <= 0) return;
        if (coins > this.coins) throw new NotEnoughCoinsException();
        this.coins -= coins;
    }

    @Override
    public String toString() {
        String entranceString = entrance.stream()
                .map(Student::toString)
                .collect(Collectors.joining("", "[", "]"));
        String towerString = String.valueOf(towers.size());
        StringBuilder diningRoomString = new StringBuilder();
        for (Color color : Color.values()) {
            char[] colorStudents = new char[maxDiningSize];
            Arrays.fill(colorStudents, '-');
            for (int i = 2; i < maxDiningSize; i += 3) colorStudents[i] = 'o';
            for (int i = 0; i < diningRoom[color.getValue()].size(); i++) colorStudents[i] = color.toString().charAt(0);
            String colorProfessor = " " + (this.hasProfessor(color) ? 'X' : ' ');
            diningRoomString.append("\n  ").append(color).append(" ").append(new String(colorStudents)).append(colorProfessor);
        }
        return "SchoolBoard " + ID + " entr:" + entranceString + diningRoomString +
                " Coins: " + coins +
                "\n    " + (towers.size() > 0 ? towers.get(0) + " towers:" + towerString : "No towers left");
    }
}
