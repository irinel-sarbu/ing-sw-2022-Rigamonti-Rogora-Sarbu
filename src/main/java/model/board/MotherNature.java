package model.board;

import java.io.Serializable;
import util.Random;

/**
 * Represents Mother Nature.
 */
public class MotherNature implements Serializable {
    private int position;

    /**
     * Constructor of {@link MotherNature}. Initializes {@link MotherNature#position}.
     */
    public MotherNature() {
        this.position = Random.nextInt(12);
    }

    /**
     * Getter for the attribute {@link MotherNature#position}.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Setter for the attribute {@link MotherNature#position} (indicates island group)
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Adds the selected number of steps to {@link MotherNature#position} in (mod N) as N=Number of remaining IslandGroups.
     *
     * @param steps   Is the selected number of steps.
     * @param islands Is the remaining number of IslandGroups.
     */
    public void progress(int steps, int islands) {
        if (steps > 0) {
            this.position = (this.position + steps) % (islands);
        } else {
            this.position = (this.position + steps + islands) % (islands);
        }

    }
}
