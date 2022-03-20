package model.expert;

public class NoEntryTile {
    private int position; //-1 when not on a tile

    public NoEntryTile() {
        this.position = -1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
