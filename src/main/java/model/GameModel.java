package model;

import events.EventSender;
import exceptions.EmptyStudentListException;
import model.board.Bag;
import model.board.CloudTile;
import model.board.IslandGroup;
import model.board.IslandTile;
import model.board.MotherNature;
import model.board.Student;
import model.expert.CoinSupply;
import exceptions.EntranceFullException;
import exceptions.IslandNotFoundException;
import exceptions.MaxPlayersException;
import util.Color;
import exceptions.PlayerNotFoundException;
import util.GameMode;
import util.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameModel extends EventSender {
    Logger logger = Logger.getLogger(GameModel.class.getName());

    public static final int MAX_PLAYERS = 3;

    private final int numOfPlayers;
    private final GameMode gameMode;
    private GameState state;
    private final Bag bag;
    private final CoinSupply coinSupply;
    private final List<IslandGroup> islandGroups;
    private final List<Player> players;
    private final List<CloudTile> cloudTiles;
    private final MotherNature motherNature;

    public GameModel(int numOfPlayers, GameMode gameMode) {
        this.numOfPlayers = numOfPlayers;
        this.gameMode = gameMode;
        this.bag = new Bag(24);
        this.coinSupply = new CoinSupply();
        this.players = new ArrayList<>();
        this.motherNature = new MotherNature();

        this.cloudTiles = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) cloudTiles.add(new CloudTile(numOfPlayers + 1));

        islandGroups = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            islandGroups.add(new IslandGroup(i));
        }

        moveFromBagToIslandTile();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Player getPlayerByID(int playerID) throws PlayerNotFoundException {
        return players.get(playerID);
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

    private void moveFromBagToIslandTile() {
        Bag initialBag = new Bag(2);
        int motherNaturePos = motherNature.getPosition();

        for (IslandGroup islandGroup : islandGroups) {
            int islandGroupPos = islandGroup.getIslandGroupID();
            if (islandGroupPos != motherNaturePos && islandGroupPos != ((motherNaturePos + 6) % 12)) {
                Student student = null;
                try {
                    student = initialBag.pull();
                } catch (EmptyStudentListException e) {
                    logger.warning("Pulling from initial bag. Something went wrong...");
                }
                islandGroup.getIslands().get(0).addStudent(student);
            }
            System.out.println(islandGroup);
        }
    }

    private void moveFromBagToEntrance(Player player) {
        for (int i = 0; i < 7; i++)
            try {
                player.getSchoolBoard().addToEntrance(bag.pull());
            } catch (EntranceFullException e) {
                logger.severe("Setting up entrance. Something went wrong...");
            } catch (EmptyStudentListException e) {
                logger.severe("Empty bag...");
            }
    }

    public IslandTile getIslandTileByID(int id) {
        for (IslandGroup ig : islandGroups) {
            IslandTile it = ig.getIslandTileByID(id);
            if (it != null)
                return it;
        }

        throw new IslandNotFoundException("Island with id " + id + " not found!");
    }

    public int getNumOfCloudTiles() {
        return cloudTiles.size();
    }

    public void refillCloudTile(int cloudTileID) {
        if (cloudTiles.get(cloudTileID).isEmpty())
            moveFromBagToCloudTile(cloudTiles.get(cloudTileID));
    }

    public void moveFromBagToCloudTile(CloudTile cloudTile) {
        int num = players.size() + 1;
        for (int i = 0; i < num; i++) {
            try {
                cloudTile.put(bag.pull());
            } catch (EmptyStudentListException e) {
                logger.severe("Pulling from initial bag. Something went wrong...");
            }
        }
    }


    public void moveMotherNature(int steps) {
        motherNature.progress(steps, islandGroups.size());
    }

    public void joinAdiacent(int islandGroupPos) {
        // TODO implement
    }

    public void moveFromCloudTileToEntrance(Color color, CloudTile cloudTile, Player player) {

    }

    // Expert mode functions
    public void fillUpCoins() {
        // TODO implement
    }

    public List<Character> getCharacters() {
        // TODO
        return null;
    }
}