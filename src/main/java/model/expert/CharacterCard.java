package model.expert;

import exceptions.EmptyNoEntryListException;
import exceptions.EmptyStudentListException;
import exceptions.StudentNotFoundException;
import model.board.Student;
import util.CharacterType;
import util.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Represents the Character of a game. If it needs additional tiles on it, it initializes them.
 */
public class CharacterCard implements Serializable {
    private int cost;
    private final CharacterType character;
    private List<Student> students;
    private Stack<NoEntryTile> noEntryTiles;
    private Color color;

    /**
     * Base constructor. Initializes all needed attributes.
     *
     * @param character Is the {@link CharacterType} of the {@link CharacterCard}.
     */
    public CharacterCard(CharacterType character) {
        this.cost = character.getBaseCost();
        this.character = character;
        switch (character) {
            case MONK, PRINCESS -> students = new ArrayList<>(4);
            case GRANNY_HERBS -> noEntryTiles = new Stack<>();
            case JESTER -> students = new ArrayList<>(6);
            case MUSHROOM_FANATIC -> color = null;
            default -> {
            }
        }
    }

    /**
     * Getter for the attribute {@link CharacterCard#color}.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter of the attribute {@link CharacterCard#color}.
     *
     * @param color Is the selected Color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter for the attribute {@link CharacterCard#character}.
     *
     * @return The {@link CharacterType} of the {@link CharacterCard}.
     */
    public CharacterType getCharacter() {
        return character;
    }

    /**
     * Getter for the attribute {@link CharacterCard#cost}.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Setter for the attribute {@link CharacterCard#cost}.
     *
     * @param cost Is the selected Cost.
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Adds a student to {@link CharacterCard#students}.
     *
     * @param student Is the selected student that needs to be added.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Getter for the attribute {@link CharacterCard#students}.
     *
     * @return The list of students initialized on the card.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Removes a student from {@link CharacterCard#students}.
     *
     * @param studentId Is the ID correspondent to the selected student that needs to be removed.
     * @return The student that needs to be removed.
     * @throws StudentNotFoundException  If there is no students with the given ID.
     * @throws EmptyStudentListException If {@link CharacterCard#students} is empty.
     */
    public Student removeStudent(int studentId) throws StudentNotFoundException, EmptyStudentListException {
        if (this.students.size() == 0) throw new EmptyStudentListException();
        for (Student student : students) {
            if (student.getID() == studentId) {
                students.remove(student);
                return student;
            }
        }
        throw new StudentNotFoundException();
    }

    /**
     * Adds a NoEntryTile to {@link CharacterCard#noEntryTiles}.
     *
     * @param noEntryTile Is the selected NoEntryTile that needs to be added.
     */
    public void addNoEntryTile(NoEntryTile noEntryTile) {
        noEntryTiles.push(noEntryTile);
    }

    /**
     * Getter for the attribute {@link CharacterCard#noEntryTiles}.
     *
     * @return The Stack of NoEntryTiles.
     */
    public Stack<NoEntryTile> getNoEntryTiles() {
        return noEntryTiles;
    }

    /**
     * Remove the first NoEntryTile from {@link CharacterCard#noEntryTiles}.
     *
     * @return The removed NoEntryTile.
     * @throws EmptyNoEntryListException If there is no more NoEntryTiles in {@link CharacterCard#noEntryTiles}.
     */
    public NoEntryTile removeNoEntryTile() throws EmptyNoEntryListException {
        if (this.noEntryTiles.size() == 0) throw new EmptyNoEntryListException();
        return noEntryTiles.pop();
    }

    /**
     * Overrides equals.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        CharacterCard that = (CharacterCard) o;
        return character == that.character;
    }

    /**
     * Overrides toString.
     */
    @Override
    public String toString() {
        switch (getCharacter()) {
            case MONK, PRINCESS, JESTER -> {
                return getCharacter().toString() + ": cost = " + getCost() + ", students: " + getStudents().toString();
            }
            case GRANNY_HERBS -> {
                return getCharacter().toString() + ": cost = " + getCost() + ", noEntryTiles: " + getNoEntryTiles().size();
            }
            default -> {
                return getCharacter().toString() + ": cost = " + getCost();
            }
        }
    }
}
