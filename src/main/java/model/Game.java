package model;

import model.board.Bag;
import model.board.IslandGroup;
import model.board.SchoolBoard;
import model.player.Team;

import java.util.List;

public class Game {
    private Game gameInstance = null;

    private SchoolBoard schoolBoard;
    private Bag bag;
    private List<IslandGroup> islandGroups;

    private List<Team> teams;

    private Game() {

    }

    public Game getInstance() {
        if(gameInstance == null)
            gameInstance = new Game();
        return gameInstance;
    }
}
