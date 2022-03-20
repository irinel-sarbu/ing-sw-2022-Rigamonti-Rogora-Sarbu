package model;

import model.board.Bag;
import model.board.IslandGroup;
import exceptions.MaxPlayersException;
import model.player.Player;
import exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game {
    Logger logger = Logger.getLogger(Game.class.getName());

    public static final int MAX_PLAYERS = 3;
    private Game gameInstance = null;

    private final Bag bag;
    private List<IslandGroup> islandGroups;
    private List<Player> players;

    private Game() {
        this.bag = new Bag(24);
        this.players = new ArrayList<>();
    }

    public Game getInstance() {
        if (gameInstance == null)
            gameInstance = new Game();
        return gameInstance;
    }

    public Player getPlayerByName(String name) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }

        throw new PlayerNotFoundException("Player " + name + " not found!");
    }

    public void addPlayer(Player player) throws MaxPlayersException {
        if (players.size() >= MAX_PLAYERS)
            throw new MaxPlayersException();
        players.add(player);
    }

    public boolean removePlayerByName(String name) {
        Player player;
        try {
            player = getPlayerByName(name);
        } catch (PlayerNotFoundException e) {
            player = null;
            logger.warning(e.getMessage());
        }
        boolean result = players.remove(player);

        return result;
    }
}