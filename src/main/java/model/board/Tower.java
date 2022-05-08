package model.board;

import model.Player;
import util.TowerColor;

import java.io.Serializable;

/**
 * Represents the Tower. The color is not Modifiable.
 */
public class Tower implements Serializable {
    private final TowerColor color;

    /**
     * Constructor of {@link Tower}. Set the final Color to the selected color.
     *
     * @param color Is the selected color.
     */
    public Tower(TowerColor color) {
        this.color = color;
    }

    /**
     * Getter of the attribute {@link Tower#color}.
     */
    public TowerColor getColor() {
        return color;
    }

    /**
     * Overrides ToString.
     */
    @Override
    public String toString() {
        return color.toString();
    }
}
