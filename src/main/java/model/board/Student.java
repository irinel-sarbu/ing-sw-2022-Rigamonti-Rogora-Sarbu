package model.board;

import util.Color;

import java.io.Serializable;

/**
 * Represents the Student. ID and Color are not Modifiable.
 */
public class Student implements Serializable {
    private final int ID;
    private final Color color;

    /**
     * Constructor of {@link Student}. Sets {@link Student#ID} and {@link Student#color} to the selected respective parameters.
     *
     * @param ID    Is the ID correspondent to the newly created Student.
     * @param color Is the Color correspondent to the newly created Student.
     */
    public Student(int ID, Color color) {
        this.ID = ID;
        this.color = color;
    }

    /**
     * Getter of the attribute {@link Student#ID}.
     */
    public int getID() {
        return ID;
    }

    /**
     * Getter of the attribute {@link Student#color}.
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Overrides toString.
     */
    @Override
    public String toString() {
        return color.toString();
    }
}
