package model.expert;

public class NoEntryTile {
    private static int count;
    private final int ID;
    private int position; //-1 when not on a tile

    public NoEntryTile() {
        ID = count;
        count++;
        this.position = -1;
    }

    public NoEntryTile(int ID) {
        this.ID = ID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
