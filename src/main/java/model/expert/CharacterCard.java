package model.expert;

import exceptions.EmptyNoEntryListException;
import exceptions.EmptyStudentListException;
import exceptions.StudentNotFoundException;
import model.board.Student;
import util.CharacterType;
import util.CliHelper;
import util.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
        switch (this.character) {
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

    public static String allToString(List<CharacterCard> boards) {
        List<String[]> splitCards = boards.stream()
                .map(CharacterCard::toCard)
                .map(bs -> bs.split("\n"))
                .toList();

        StringBuilder cards = new StringBuilder();

        for (int y = 0; y < splitCards.get(0).length; y++) {
            for (String[] splitCard : splitCards) {
                cards.append(String.format("%-20s ", splitCard[y]));
            }
            cards.append("\n");
        }

        return cards.toString();
    }

    public String toCard() {
        switch (this.character) {
            case MONK, PRINCESS, JESTER -> {
                return createStudentsCard();
            }
            case GRANNY_HERBS -> {
                return createNoEntryCard();
            }
            default -> {
                return createNormalCard();
            }
        }
    }

    private String createNormalCard() {
        String[] nameSplit = character.getName().split("\\s+");
        StringBuilder card = new StringBuilder();
        card.append("╭────────────╮\n");
        card.append("│ ").append(String.format("%10s", String.format("Cost: %-3d ", cost))).append(" │\n");
        card.append("│ ").append(String.format("%10s", "")).append(" │\n");
        card.append("│ ").append(String.format("%10s", "")).append(" │\n");
        card.append("│ ").append(String.format("%10s", nameSplit[0])).append(" │\n");
        card.append("│ ").append(String.format("%10s", nameSplit.length > 1 ? nameSplit[1] : "")).append(" │\n");
        card.append("│ ").append(String.format("%10s", "")).append(" │\n");
        card.append("│ ").append(String.format("%10s", "")).append(" │\n");
        card.append("╰────────────╯\n");

        return card.toString();
    }

    private String createStudentsCard() {
        String[] nameSplit = character.getName().split("\\s+");
        StringBuilder card = new StringBuilder();
        card.append("╭────────────┬─────╮\n");
        card.append("│ ").append(String.format("%10s", String.format("Cost: %-3d ", cost))).append(" │").append(createRow(10, 10, true));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(1, 2, true));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(10, 10, true));
        card.append("│ ").append(String.format("%10s", nameSplit[0])).append(" │").append(createRow(3, 4, true));
        card.append("│ ").append(String.format("%10s", nameSplit.length > 1 ? nameSplit[1] : "")).append(" │").append(createRow(10, 10, true));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(5, 6, true));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(10, 10, true));
        card.append("╰────────────┴─────╯\n");

        return card.toString();
    }

    private String createNoEntryCard() {
        String[] nameSplit = character.getName().split("\\s+");
        StringBuilder card = new StringBuilder();
        card.append("╭────────────┬─────╮\n");
        card.append("│ ").append(String.format("%10s", String.format("Cost: %-3d ", cost))).append(" │").append(createRow(10, 10, false));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(1, 2, false));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(10, 10, false));
        card.append("│ ").append(String.format("%10s", nameSplit[0])).append(" │").append(createRow(3, 4, false));
        card.append("│ ").append(String.format("%10s", nameSplit.length > 1 ? nameSplit[1] : "")).append(" │").append(createRow(10, 10, false));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(5, 6, false));
        card.append("│ ").append(String.format("%10s", "")).append(" │").append(createRow(10, 10, false));
        card.append("╰────────────┴─────╯\n");

        return card.toString();
    }

    private String createRow(int min, int max, boolean student) {
        String row = "";
        boolean placeHolder1;
        if (student) {
            placeHolder1 = students.size() >= min;
        } else {
            placeHolder1 = noEntryTiles.size() >= min;
        }

        boolean placeHolder2;
        if (student) {
            placeHolder2 = students.size() >= max;
        } else {
            placeHolder2 = noEntryTiles.size() >= max;
        }

        if (placeHolder1) {
            if (student) {
                row += " " + CliHelper.getStudentIcon(students.get(min - 1).getColor());
            } else {
                row += " " + CliHelper.getNoEntryIcon();
            }
            if (placeHolder2) {
                if (student) {
                    row += " " + CliHelper.getStudentIcon(students.get(max - 1).getColor()) + " │\n";
                } else {
                    row += " " + CliHelper.getNoEntryIcon() + " │\n";
                }
            } else {
                row += "   │\n";
            }
        } else {
            row += "     │\n";
        }

        return row;
    }
}
