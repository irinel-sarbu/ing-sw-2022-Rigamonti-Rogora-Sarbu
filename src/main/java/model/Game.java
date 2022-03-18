package model;

import model.board.Bag;
import model.board.IslandGroup;
import model.player.Team;

import java.util.List;

public class Game {
    private Game gameInstance = null;

    private Bag bag = null;
    private List<IslandGroup> islandGroups = null;

    private List<Team> teams = null;

    private Game() {
    }

    public Game getInstance() {
        if(gameInstance == null)
            gameInstance = new Game();
        return gameInstance;
    }
}
