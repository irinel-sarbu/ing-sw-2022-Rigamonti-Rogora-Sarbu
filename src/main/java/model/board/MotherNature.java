package model.board;

import java.util.Random;

public class MotherNature {
    private int position;

    public MotherNature() {
        Random rng = new Random();
        this.position = rng.nextInt(12);
    }

    public int getPosition() {
        return position;
    }

    public void progress(int steps, int islands) {
        if(steps>0){
            this.position = (this.position + steps) % (islands-1);
        }else{
            this.position = (this.position + steps + islands-1) % (islands-1);
        }

    }
}
