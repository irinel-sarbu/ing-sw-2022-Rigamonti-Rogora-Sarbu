package model.expert;

/**
 * Represents the No Entry tile, which denies the access to an IslandGroup once.
 */
public class NoEntryTile {
    private int position; //-1 when not on a tile

    /**
     * Constructor of NoEntryTile. Sets the {@link NoEntryTile#position} to -1 since it's initialized on {@link util.CharacterType#GRANNY_HERBS}.
     */
    public NoEntryTile() {
        this.position = -1;
    }

    /**
     *Getter for the attribute {@link NoEntryTile#position}.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Setter for the attribute {@link NoEntryTile#position}.
     * @param position Is the selected position.
     */
    public void setPosition(int position) {
        this.position = position;
    }
}
