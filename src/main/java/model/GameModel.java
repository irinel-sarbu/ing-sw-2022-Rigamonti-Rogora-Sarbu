package model;

import observer.Observable;
import exceptions.*;
import model.board.*;
import model.expert.*;
import util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel extends Observable {
    private final int maxNumOfPlayers;
    private final GameMode gameMode;
    private GameState state;
    private final Bag bag;

    private final List<IslandGroup> islandGroups;
    private final List<Player> players;
    private final List<CloudTile> cloudTiles;
    private final MotherNature motherNature;
    private List<CharacterCard> characters;
    private CoinSupply coinSupply;

    public GameModel(int maxNumOfPlayers, GameMode gameMode) {
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.gameMode = gameMode;
        this.bag = new Bag(24);
        this.players = new ArrayList<>();
        this.motherNature = new MotherNature();

        if (gameMode == GameMode.EXPERT) {
            this.characters = new ArrayList<>();
            this.coinSupply = new CoinSupply(20);
            drawThreeCharacters();
        }

        this.cloudTiles = new ArrayList<>();
        for (int i = 0; i < maxNumOfPlayers; i++) cloudTiles.add(new CloudTile(maxNumOfPlayers + 1));

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
        return maxNumOfPlayers;
    }

    public int getPlayerSize() {
        return players.size();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Player getPlayerByID(int id) throws PlayerNotFoundException {
        Player ret = players.get(id);
        if (ret == null)
            throw new PlayerNotFoundException();

        return ret;
    }

    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player p : players)
            playerNames.add(p.getName());
        return playerNames;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public synchronized Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }

        return null;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean removePlayerByName(String name) {
        Player player;
        player = getPlayerByName(name);

        return players.remove(player);
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
                    Logger.info("Pulling from initial bag. Something went wrong...");
                }
                islandGroup.getIslands().get(0).addStudent(student);
            }
        }
    }

    private void moveFromBagToEntrance(Player player) {
        for (int i = 0; i < 7; i++)
            try {
                player.getSchoolBoard().addToEntrance(bag.pull());
            } catch (EntranceFullException e) {
                Logger.error("Setting up entrance. Something went wrong...");
            } catch (EmptyStudentListException e) {
                Logger.error("Empty bag...");
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
                Logger.error("Pulling from initial bag. Something went wrong...");
            }
        }
    }

    public void moveMotherNature(int steps) {
        motherNature.progress(steps, islandGroups.size());
    }

    public void joinAdjacent() {
        for (IslandGroup islandGroup : islandGroups) {
            int index = islandGroups.indexOf(islandGroup);
            IslandGroup left = islandGroups.get((index - 1) % islandGroups.size());
            IslandGroup right = islandGroups.get((index + 1) % islandGroups.size());

            try {
                left.join(islandGroup);
                islandGroup.join(right);
            } catch (IllegalIslandGroupJoinException | NullIslandGroupException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveFromCloudTileToEntrance(CloudTile cloudTile, Player player) {
        try {
            player.getSchoolBoard().addToEntrance(cloudTile.getAndRemoveStudents());
        } catch (EntranceFullException e) {
            e.printStackTrace();
        }
    }

    // Expert mode functions
    private void drawThreeCharacters() {
        for (int i = 0; i < 3; i++) characters.add(getRandomCharacter());
    }

    private CharacterCard getRandomCharacter() {
        CharacterCard character;
        do {
            int pick = new Random().nextInt(CharacterType.values().length);
            character = new CharacterCard(CharacterType.values()[pick]);
        } while (characters.contains(character));
        switch (character.getCharacter()) {
            case MONK, PRINCESS -> {
                for (int i = 0; i < 4; i++) character.addStudent(bag.pull());
            }
            case GRANNY_HERBS -> {
                for (int i = 0; i < 4; i++) character.addNoEntryTile(new NoEntryTile());
            }
            case JESTER -> {
                for (int i = 0; i < 6; i++) character.addStudent(bag.pull());
            }
            default -> {
            }
        }
        return character;
    }

    public List<CharacterCard> getCharacters() {
        return characters;
    }

    public CharacterCard getCharacterByType(CharacterType characterType) {
        for (CharacterCard characterCard : characters) {
            if (characterCard.getCharacter().equals(characterType)) {
                return characterCard;
            }
        }
        return null;
    }
}