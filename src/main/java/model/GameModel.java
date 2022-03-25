package model;

import events.EventSender;
import model.board.Bag;
import model.board.CloudTile;
import model.board.IslandGroup;
import model.board.MotherNature;
import model.board.Student;
import exceptions.EntranceFullException;
import exceptions.MaxPlayersException;
import model.player.Player;
import exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameModel extends EventSender {
    Logger logger = Logger.getLogger(GameModel.class.getName());

    public static final int MAX_PLAYERS = 3;

    private final Bag bag;
    private final List<IslandGroup> islandGroups;
    private final List<Player> players;
    private final MotherNature motherNature;

    public GameModel() {
        this.bag = new Bag(24);
        this.players = new ArrayList<>();
        this.motherNature = new MotherNature();

        islandGroups = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            islandGroups.add(new IslandGroup(i));
        }

        init();
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

    private void init() {
        moveFromBagToIslandTile();

        for(Player p : players) {
            moveFromBagToEntrance(p);
        }
    }

    private void moveFromBagToIslandTile() {
        Bag initialBag = new Bag(2);
        int motherNaturePos = motherNature.getPosition();

        for (IslandGroup islandGroup : islandGroups) {
            int islandGroupPos = islandGroup.getIslandGroupID();
            if (islandGroupPos != motherNaturePos && islandGroupPos != ((motherNaturePos + 6) % 12)) {
                Student student = initialBag.pull();
                islandGroup.getIslandTileByID(0).addStudent(student);
            }
            System.out.println(islandGroup);
        }
    }

    private void moveFromBagToEntrance(Player player) {
        for(int i = 0; i < 7; i++) 
            try {
                player.getSchoolBoard().addToEntrance(bag.pull());
            } catch (EntranceFullException e) {
                logger.warning("Setting up entrance. Something went wrong...");
            }
    }

    public void moveFromBagToCloudTile(CloudTile cloudTile) {
        int num = players.size() + 1;
        for(int i = 0; i < num; i++)
            cloudTile.put(bag.pull());
    }

    public void moveFromCloudTileToEntrance(CloudTile cloudTile, Player player) {
        
    }

    
}