package model.board;

import java.util.*;
import java.util.stream.Collectors;

import exceptions.*;
import model.Player;
import model.expert.CoinSupply;
import util.Color;
import util.TowerColor;

/**
 * Represents the School Board. Is identified by the Player where it is created on.
 */
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

    /**
     * Constructor of {@link SchoolBoard}. Initializes all attributes. Sets the {@link SchoolBoard#owner} to the Parameter Player.
     *
     * @param player Is the owner of this {@link SchoolBoard}.
     */
    public SchoolBoard(Player player) {
        this.coins = new CoinSupply(0);
        this.entrance = new ArrayList<>();
        this.diningRoom = new ArrayList<>();
        for (int i = 0; i < Color.values().length; i++) {
            diningRoom.add(i, new Stack<>());
        }
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.owner = player;
    }

    /**
     * Getter for the students on the {@link SchoolBoard#entrance}.
     *
     * @return The List of student on the {@link SchoolBoard#entrance}.
     */
    public List<Student> getEntranceStudents() {
        return entrance;
    }

    /**
     * Getter for a selected student on the {@link SchoolBoard#entrance}.
     *
     * @param studentPosition Is the position of the {@link SchoolBoard#entrance} correspondent to the selected student.
     * @return The selected student.
     * @throws StudentNotFoundException If there is no Student on the given Position.
     */
    public Student getEntranceStudent(int studentPosition) throws StudentNotFoundException {
        Student student = entrance.get(studentPosition);
        if (student == null) throw new StudentNotFoundException();
        return student;
    }

    /**
     * Adds a Student on the {@link SchoolBoard#entrance}.
     *
     * @param student Is the student that needs to be added.
     * @throws EntranceFullException If the entrance is already full.
     */
    public void addToEntrance(Student student) throws EntranceFullException {
        if (entrance.size() >= maxEntranceSize) throw new EntranceFullException();
        entrance.add(student);
    }

    /**
     * Adds multiple students to the {@link SchoolBoard#entrance} at the same time.
     *
     * @param students Is the List of students that needs to be added.
     * @throws EntranceFullException If the entrance is already full.
     */
    public void addToEntrance(List<Student> students) throws EntranceFullException {
        for (Student student : students) {
            if (entrance.size() >= maxEntranceSize) throw new EntranceFullException();
            entrance.add(student);
        }
    }

    /**
     * Removes a selected student from the {@link SchoolBoard#entrance}.
     *
     * @param studentID
     * @return
     * @throws StudentNotFoundException
     */
    public Student removeFromEntrance(int studentID) throws StudentNotFoundException {
        Student removed = null;
        for (int i = 0; i < entrance.size(); i++) {
            if (entrance.get(i).getID() == studentID) {
                removed = entrance.remove(i);
                break;
            }
        }
        if (removed == null) throw new StudentNotFoundException();
        return removed;
    }

    /**
     * Adds a selected student to the {@link SchoolBoard#diningRoom}.
     *
     * @param student Is the selected student that needs to be added.
     * @return True if the number of students of a certain color is 0(mod3). Player will have to pick up a coin from the stash.
     * @throws DiningRoomFullException if the Dining Room is already full.
     */
    public boolean addToDiningRoom(Student student) throws DiningRoomFullException {
        if (diningRoom.get(student.getColor().getValue()).size() + 1 > maxDiningSize) {
            throw new DiningRoomFullException();
        }
        diningRoom.get(student.getColor().getValue()).push(student);
        return (diningRoom.get(student.getColor().getValue()).size() % 3) == 0;
    }

    /**
     * Getter for the Owner of this {@link SchoolBoard} personal coin supply.
     */
    public CoinSupply getCoinSupply() {
        return coins;
    }

    /**
     * Removes a selected student from {@link SchoolBoard#diningRoom}.
     *
     * @param color Is the color correspondent to the last student of the respective dining room table.
     * @return The selected student.
     * @throws DiningRoomEmptyException If the dining room is already Empty.
     */
    public Student removeFromDiningRoom(Color color) throws DiningRoomEmptyException {
        if (diningRoom.get(color.getValue()).size() == 0) {
            throw new DiningRoomEmptyException();
        }
        return diningRoom.get(color.getValue()).pop();
    }

    /**
     * Getter for the number of students of a selected table color from {@link SchoolBoard#diningRoom}.
     *
     * @param color Is the selected color.
     * @return The number of students of that color.
     */
    public int getStudentsOfColor(Color color) {
        return diningRoom.get(color.getValue()) == null ? 0 :
                diningRoom.get(color.getValue()).size();
    }

    /**
     * Getter for the attribute {@link SchoolBoard#professors}.
     *
     * @return The List of professors.
     */
    public List<Professor> getProfessors() {
        return professors;
    }

    /**
     * Adds a selected professor to {@link SchoolBoard#professors}.
     *
     * @param professor Is the selected professor.
     * @throws ProfessorFullException If the professor List is already full.
     */
    public void addProfessor(Professor professor) throws ProfessorFullException {
        if (professors.size() >= maxProfessorsSize) throw new ProfessorFullException();
        professors.add(professor);
    }

    /**
     * Removes a selected Professor from {@link SchoolBoard#professors}.
     *
     * @param professor Is the selected professor.
     * @throws ProfessorNotFoundException If the professor is not in the List.
     */
    public void removeProfessor(Professor professor) throws ProfessorNotFoundException {
        boolean success;
        success = professors.remove(professor);
        if (!success) throw new ProfessorNotFoundException();
    }

    /**
     * Removes a professor of a selected color from {@link SchoolBoard#professors}.
     *
     * @param color Is the selected color.
     * @return The professor correspondent to the selected color. Null if there is no professor with the given color.
     */
    public Professor removeProfessorByColor(Color color) {
        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getColor().equals(color)) return professors.remove(i);
        }
        //throw new ProfessorNotFoundException(); if it is try caught elsewhere remove it.
        return null;
    }

    /**
     * Getter for the attribute {@link SchoolBoard#towers}.
     *
     * @return The List of {@link SchoolBoard#towers}.
     */
    public List<Tower> getTowers() {
        return towers;
    }

    /**
     * Adds a New tower to {@link SchoolBoard#towers}.
     *
     * @param tower Is the New tower.
     * @throws TowersFullException If {@link SchoolBoard#towers} is already full.
     */
    public void addTower(Tower tower) throws TowersFullException {
        if (towers.size() >= maxTowersSize) throw new TowersFullException();
        towers.add(tower);
    }

    /**
     * Removes a tower from {@link SchoolBoard#towers}.
     *
     * @throws TowersIsEmptyException If {@link SchoolBoard#towers} is already empty.
     */
    public void removeTower() throws TowersIsEmptyException {
        boolean success = towers.remove(0) != null;
        if (!success) throw new TowersIsEmptyException();
    }

    /**
     * Initializes {@link SchoolBoard#towers} by creating N new towers, where N = Right number pf tower based on the number of players.
     *
     * @param color           Is the color of the New towers that needs to be created.
     * @param maxNumOfPlayers Is the number of players that allows to pick the correct number of towers.
     */
    public void setUpTowers(TowerColor color, int maxNumOfPlayers) {
        int numOfTowers;
        if (maxNumOfPlayers == 2) numOfTowers = 8;
        else numOfTowers = 6;
        try {
            for (int i = 0; i < numOfTowers; i++) {
                addTower(new Tower(color));
            }
        } catch (TowersFullException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a professor of the selected color is present in {@link SchoolBoard#professors}.
     *
     * @param color Is the selected color.
     * @return True if it is present, otherwise False.
     */
    public boolean hasProfessor(Color color) {
        for (Professor professor : professors) {
            if (professor.getColor().equals(color)) return true;
        }
        return false;
    }

    /* Unused ToString.
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
    }*/

    /**
     * Overrides toString.
     */
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
