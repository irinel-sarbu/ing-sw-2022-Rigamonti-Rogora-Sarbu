package model;

public class Turn {
    private Player player;
    private int studentsMoved;
    private final int maxStudentMovements;

    public Turn(int maxStudentMovements){
        this.maxStudentMovements=maxStudentMovements;
        this.studentsMoved=0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getStudentsMoved() {
        return studentsMoved;
    }

    public void setStudentsMoved(int studentsMoved) {
        this.studentsMoved = studentsMoved;
    }

    public void resetStudentsMoved(){
        this.studentsMoved=0;
    }

    public int getMaxStudentMovements() {
        return maxStudentMovements;
    }
}
