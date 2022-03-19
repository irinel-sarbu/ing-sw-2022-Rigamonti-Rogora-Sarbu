package model.board;

public class MotherNature {
    private int position;

    public MotherNature(){
        this.position=0;
    }

    public int getPosition() {
        return position;
    }

    public void progress(int steps, int islands) {
        this.position = (this.position + steps) % islands;
    }
}
