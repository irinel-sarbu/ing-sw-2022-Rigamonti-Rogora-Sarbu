package model.board;

import util.Color;

import java.io.Serializable;

/**
 * Represents the professor. His Color is not Modifiable.
 */
public class Professor implements Serializable {
    private final Color color;

    /**
     * Constructor of {@link Professor}. Sets {@link Professor#color} to the selected Color.
     *
     * @param color Is the selected Color
     */
    public Professor(Color color) {
        this.color = color;
    }

    /**
     * Getter of the attribute {@link Professor#color}.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Overrides toString.
     */
    @Override
    public String toString() {
        return color.toString() + " Prof.";
    }
}
