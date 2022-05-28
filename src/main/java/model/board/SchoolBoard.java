package model.board;

import exceptions.*;
import model.Player;
import model.expert.CoinSupply;
import util.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the School Board. Is identified by the Player where it is created on.
 */
public class SchoolBoard implements Serializable {
    private final static int maxEntranceSize = 9;
    private final List<Professor> professors;
    private final static int maxProfessorsSize = Color.values().length;
    private final static int maxTowersSize = 8;
    private final static int maxDiningSize = 10;
    private final List<Student> entrance;
    private final List<Stack<Student>> diningRoom;
    private final List<Tower> towers;
    private final CoinSupply coins;
    private final Player owner;

    private TowerColor towerColor;

    /**
     * Constructor of {@link SchoolBoard}. Initializes all attributes. Sets the {@link SchoolBoard#owner} to the Parameter Player.
     *
     * @param player Is the owner of this {@link SchoolBoard}.
     */
    public SchoolBoard(Player player) {
        if (player.getGameMode() == GameMode.EXPERT) {
            this.coins = new CoinSupply(0);
        } else {
            this.coins = null;
        }
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
     * @param studentID Is the ID of the student in entrance {@link SchoolBoard#entrance} correspondent to the selected student.
     * @return The selected student.
     * @throws StudentNotFoundException If there is no Student on the given Position.
     */
    public Student getEntranceStudent(int studentID) throws StudentNotFoundException {
        Student student = null;
        for (Student value : entrance) {
            if (value.getID() == studentID) {
                student = value;
                break;
            }
        }

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
     * @param studentID ID of the student to remove
     * @return the removed {@link Student}
     * @throws StudentNotFoundException if the specified student is not found
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
        this.towerColor = tower.getColor();
        towers.add(tower);
    }

    /**
     * Removes a tower from {@link SchoolBoard#towers}.
     *
     * @throws TowersIsEmptyException If {@link SchoolBoard#towers} is already empty.
     */
    public void removeTower() throws TowersIsEmptyException {
        if (towers.size() == 0) throw new TowersIsEmptyException();
        towers.remove(0);
    }

    /**
     * Initializes {@link SchoolBoard#towers} by creating N new towers, where N = Right number pf tower based on the number of players.
     *
     * @param color           Is the color of the New towers that needs to be created.
     * @param maxNumOfPlayers Is the number of players that allows to pick the correct number of towers.
     */
    public void setupTowers(TowerColor color, int maxNumOfPlayers) {
        int numOfTowers;
        if (maxNumOfPlayers == 2) numOfTowers = 8;
        else numOfTowers = 6;
        try {
            for (int i = 0; i < numOfTowers; i++) {
                addTower(new Tower(color));
            }
        } catch (TowersFullException e) {
            Logger.warning("Can't setup towers, there are already some towers");
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

    public static String allToString(List<SchoolBoard> boards) {
        List<String[]> splitBoards = boards.stream()
                .map(SchoolBoard::toString)
                .map(bs -> bs.split("\n"))
                .toList();

        StringBuilder table = new StringBuilder();

        for (int y = 0; y < splitBoards.get(0).length; y++) {
            for (String[] board : splitBoards) {
                table.append(String.format("%-38s ", board[y]));
            }
            table.append("\n");
        }

        return table.toString();
    }

    /**
     * Overrides toString.
     */
    @Override
    public String toString() {
        String schoolBoard = "Schoolboard of " + String.format("%-23s", owner.getName()) + "\n";

        schoolBoard += "╭────┬─────────────────────┬───┬─────╮\n";
        schoolBoard += buildRow(0, Color.YELLOW, false, 0, 0);
        schoolBoard += buildRow(1, Color.YELLOW, true, 1, 2);
        schoolBoard += buildRow(2, Color.BLUE, false, 0, 0);
        schoolBoard += buildRow(3, Color.BLUE, true, 3, 4);
        schoolBoard += buildRow(4, Color.GREEN, false, 0, 0);
        schoolBoard += buildRow(5, Color.GREEN, true, 5, 6);
        schoolBoard += buildRow(6, Color.RED, false, 0, 0);
        schoolBoard += buildRow(7, Color.RED, true, 7, 8);
        schoolBoard += buildRow(8, Color.PINK, false, 0, 0);
        schoolBoard += "╰────┴─────────────────────┴───┴─────╯\n";

        if (owner.getGameMode() == GameMode.EXPERT) {
            schoolBoard += String.format("Coins: %-31d", coins.getNumOfCoins()) + "\n";
        }

        return schoolBoard;
    }

    private String buildRow(int rowIndex, Color color, boolean studentRow, int tMin, int tMax) {
        int studentsInEntrance = entrance.size();

        String entranceStudent = studentsInEntrance > rowIndex ? CliHelper.getStudentIcon(entrance.get(rowIndex).getColor()) : "●";
        StringBuilder row = new StringBuilder();

        if (studentRow) {
            int numOfTowers = towers.size();
            boolean tower1 = numOfTowers >= tMin;
            boolean tower2 = numOfTowers >= tMax;
            row.append("│  ").append(entranceStudent).append(" │ ");
            row.append("                    │   │ ");

            if (tower1) {
                row.append(CliHelper.getTowerIcon(this.towerColor)).append(" ");
                if (tower2) {
                    row.append(CliHelper.getTowerIcon(this.towerColor));
                } else {
                    row.append(" ");
                }
            } else {
                row.append("   ");
            }

            row.append(" │");
            return row.toString() + "\n";
        }

        row.append("│ ").append(entranceStudent).append("  │ ");
        Stack<Student> colorStudents = diningRoom.get(color.getValue());
        for (int x = 0; x < 10; x++) {
            if (x < colorStudents.size()) {
                row.append(CliHelper.getStudentIcon(color)).append(" ");
            } else {
                if (x % 3 == 2) {
                    row.append(CliHelper.ANSI_GOLD + "□ " + CliHelper.ANSI_RESET);
                } else {
                    row.append("● ");
                }
            }
        }
        row.append("│ ");
        boolean hasProf = false;
        for (Professor professor : professors) {
            if (professor.getColor() == color) {
                hasProf = true;
                break;
            }
        }
        row.append(hasProf ? CliHelper.getProfessorIcon(color) : " ");
        row.append(" │     │");

        return row.toString() + "\n";
    }
}
