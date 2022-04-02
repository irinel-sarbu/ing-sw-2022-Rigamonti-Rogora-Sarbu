package model.board;

import java.util.*;
import java.util.stream.Collectors;

import exceptions.*;
import model.Player;
import model.expert.CoinSupply;
import util.Color;

public class SchoolBoard {
    private final static int maxEntranceSize = 9;
    private final List<Professor> professors;
    private final static int maxProfessorsSize = Color.values().length;
    private final static int maxTowersSize = 8;
    private final static int maxDiningSize = 10;
    private final List<Student> entrance;
    private final List<Stack<Student>> diningRoom;
    private final List<Tower> towers;
    private CoinSupply coins;
    private final Player owner;

    public SchoolBoard(Player player) {
        this.coins = new CoinSupply();
        this.entrance = new ArrayList<>();
        this.diningRoom = new ArrayList<>(Color.values().length);
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.owner = player;
    }

    public List<Student> getEntranceStudents() {
        return entrance;
    }

    public Student getEntranceStudent(int studentPosition) throws StudentNotFoundException {
        Student student = entrance.get(studentPosition);
        if (student == null) throw new StudentNotFoundException();
        return student;
    }

    public void addToEntrance(Student student) throws EntranceFullException {
        if (entrance.size() >= maxEntranceSize) throw new EntranceFullException();
        entrance.add(student);
    }

    public void addToEntrance(List<Student> students) throws EntranceFullException {
        for (Student student : students) {
            if (entrance.size() >= maxEntranceSize) throw new EntranceFullException();
            entrance.add(student);
        }
    }

    public Student removeFromEntrance(int position) throws StudentNotFoundException {
        Student removed = entrance.remove(position);
        if (removed == null) throw new StudentNotFoundException();
        return removed;
    }

    public boolean addToDiningRoom(Student student) throws DiningRoomFullException {
        if (diningRoom.get(student.getColor().getValue()).size() + 1 > maxDiningSize) {
            throw new DiningRoomFullException();
        }
        diningRoom.get(student.getColor().getValue()).push(student);
        return (diningRoom.get(student.getColor().getValue()).size() % 3) == 0;
    }

    public CoinSupply getCoinSupply() {
        return coins;
    }

    public Student removeFromDiningRoom(Color color) throws DiningRoomEmptyException {
        if (diningRoom.get(color.getValue()).size() == 0) {
            throw new DiningRoomEmptyException();
        }
        return diningRoom.get(color.getValue()).pop();
    }

    public int getStudentsOfColor(Color color) {
        return diningRoom.get(color.getValue()).size();
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
            for (int i = 0; i < diningRoom.get(color.getValue()).size(); i++)
                colorStudents[i] = color.toString().charAt(0);
            String colorProfessor = " " + (this.hasProfessor(color) ? 'X' : ' ');
            diningRoomString.append("\n  ").append(color).append(" ").append(new String(colorStudents)).append(colorProfessor);
        }
        //SchoolBoard will be identified by the previous print of a numbered player.
        return "SchoolBoard of " + owner.getName() + " entr:" + entranceString + diningRoomString +
                " Coins: " + coins +
                "\n    " + (towers.size() > 0 ? towers.get(0) + " towers:" + towerString : "No towers left");
    }
}
