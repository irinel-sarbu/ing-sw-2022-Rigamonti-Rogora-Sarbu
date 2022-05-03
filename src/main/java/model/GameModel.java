package model;

import exceptions.*;
import model.board.*;
import model.expert.CharacterCard;
import model.expert.CoinSupply;
import model.expert.NoEntryTile;
import util.CharacterType;
import util.Color;
import util.GameMode;
import util.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GameModel Class represent the model. It is used to communicate with the controller.
 */
public class GameModel {
    private final int maxNumOfPlayers;
    private final GameMode gameMode;
    private final Bag bag;

    private final List<IslandGroup> islandGroups;
    private final List<Player> players;
    private final List<CloudTile> cloudTiles;
    private final MotherNature motherNature;
    private final Set<Color> unassignedProfessors;

    private List<CharacterCard> characters;
    private CoinSupply coinSupply;

    private CharacterType activeCharacterEffect;

    /**
     * Constructor of the GameModel, initializes all attributes.
     *
     * @param maxNumOfPlayers Number of players chosen when the game is created. Can be 2 or 3.
     * @param gameMode        {@link GameMode} chosen when the game is created. Can be EXPERT or NORMAL.
     */
    public GameModel(int maxNumOfPlayers, GameMode gameMode) {
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.gameMode = gameMode;
        this.bag = new Bag(24);
        this.players = new ArrayList<>();
        this.motherNature = new MotherNature();

        unassignedProfessors = Arrays.stream(Color.values()).collect(Collectors.toSet());

        if (gameMode == GameMode.EXPERT) {
            this.characters = new ArrayList<>();
            this.coinSupply = new CoinSupply(20);
            drawThreeCharacters();
        }

        this.cloudTiles = new ArrayList<>();
        //CloudTileId is generated with the i in the for
        for (int i = 0; i < maxNumOfPlayers; i++) {
            cloudTiles.add(new CloudTile(i, maxNumOfPlayers + 1));
            refillCloudTile(i);
        }

        islandGroups = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            islandGroups.add(new IslandGroup(i));
        }

        activeCharacterEffect = null;

        moveFromBagToIslandTile();
    }

    /**
     * Getter for the attribute {@link GameModel#coinSupply}.
     */
    public CoinSupply getCoinSupply() {
        return coinSupply;
    }

    /**
     * Getter for the attribute {@link GameModel#motherNature}.
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     * Getter for the attribute {@link GameModel#maxNumOfPlayers}.
     */
    public int getMaxNumOfPlayers() {
        return maxNumOfPlayers;
    }

    /**
     * Getter for the size of {@link GameModel#players}.
     *
     * @return The number of players added in the game when the method is called.
     */
    public int getPlayerSize() {
        return players.size();
    }

    /**
     * Getter for the attribute {@link GameModel#gameMode}, of {@link GameMode} Type.
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Getter for the selected Player from {@link GameModel#players}.
     *
     * @param id Is the ID correspondent to the position in the ArrayList of the selected Player.
     * @return The selected Player.
     * @throws PlayerNotFoundException If there is no Player with the given ID.
     */
    public Player getPlayerByID(int id) throws PlayerNotFoundException {
        if (id >= players.size() || id < 0)
            throw new PlayerNotFoundException();
        Player ret = players.get(id);
        return ret;
    }

    /**
     * Getter for the ID of a selected Player from {@link GameModel#players}.
     *
     * @param player Is the selected Player of {@link Player} Type.
     * @return The ID correspondent to the position in the ArrayList of the selected Player.
     */
    public int getPlayerId(Player player) {
        return players.indexOf(player);
    }

    /**
     * Getter for the Unique names of Players contained in {@link GameModel#players}.
     *
     * @return an ArrayList of Strings containing the names of the players.
     */
    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player p : players)
            playerNames.add(p.getName());
        return playerNames;
    }

    /**
     * Getter for the attribute {@link GameModel#players}.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Getter for the selected Player from {@link GameModel#players}.
     *
     * @param name Is the Unique name correspondent to the selected Player in {@link GameModel#players}.
     * @return The selected Player.
     * @throws PlayerNotFoundException If there is no Player with the given name.
     */
    public Player getPlayerByName(String name) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }

        throw new PlayerNotFoundException("Player " + name + " not found!");
    }

    /**
     * Adds a player to the attribute {@link GameModel#players}.
     *
     * @param player Is the player that needs to be added.
     */
    public void addPlayer(Player player) {
        players.add(player);
        moveFromBagToEntrance(player);
        player.getSchoolBoard().setupTowers(player.getColor(), maxNumOfPlayers);
    }

    /**
     * Removes the selected Player from {@link GameModel#players}.
     * Handles PlayerNotFoundException.
     *
     * @param name Is the unique name correspondent to the selected player that needs to be removed.
     * @return True if the operation succeeded, False otherwise.
     */
    public void removePlayerByName(String name) {
        Player player;
        try {
            player = getPlayerByName(name);
        } catch (PlayerNotFoundException e) {
            player = null;
            Logger.warning(e.getMessage());
        }

        players.remove(player);
    }

    /**
     * Method Called in the constructor that setups the first students on the initial islands.
     */
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
                    Logger.warning("Pulling from initial bag. Something went wrong...");
                }
                islandGroup.getIslands().get(0).addStudent(student);
            }
            //System.out.println(islandGroup);
        }
    }

    /**
     * Draws the initial 7 students to the entrance of a selected Player.
     *
     * @param player Is the selected player.
     */
    private void moveFromBagToEntrance(Player player) {
        for (int i = 0; i < 7; i++)
            try {
                player.getSchoolBoard().addToEntrance(bag.pull());
            } catch (EntranceFullException e) {
                Logger.severe("Setting up entrance. Something went wrong...");
            } catch (EmptyStudentListException e) {
                Logger.severe("Empty bag...");
            }
    }

    /**
     * Getter of a selected IslandGroup from {@link GameModel#islandGroups}.
     *
     * @param id Is the ID correspondent to the selected IslandGroup.
     * @return The selected IslandGroup.
     * @throws IslandGroupNotFoundException If there is no IslandGroup with the given ID.
     */
    public IslandGroup getIslandGroupByID(int id) /* throws IslandGroupNotFoundException */ {
        /*if (islandGroups.get(id) == null)
            throw new IslandGroupNotFoundException();*/
        return islandGroups.get(id);
    }

    /**
     * Getter of a selected IslandTile contained by one of the IslandGroups in {@link GameModel#islandGroups}.
     * Handles IslandNotFoundException.
     *
     * @param id Is the unique ID of the selected IslandTile.
     * @return the selected IslandTile.
     */
    public IslandTile getIslandTileByID(int id) {
        for (IslandGroup ig : islandGroups) {
            IslandTile it = ig.getIslandTileByID(id);
            if (it != null)
                return it;
        }
        throw new IslandNotFoundException("Island with id " + id + " not found!");
    }

    /**
     * Getter for the size of the attribute {@link GameModel#cloudTiles}.
     *
     * @return The number of CloudTiles contained in {@link GameModel#cloudTiles}.
     */
    public int getNumOfCloudTiles() {
        return cloudTiles.size();
    }

    /**
     * Draws the students on the selected CloudTile by calling {@link GameModel#moveFromBagToCloudTile(CloudTile)}.
     *
     * @param cloudTileID Is the ID correspondent to the selected CloudTile.
     */
    public void refillCloudTile(int cloudTileID) {
        try {
            moveFromBagToCloudTile(cloudTiles.get(cloudTileID));
        } catch (TooManyStudentsException e) {
            // do Nothing
        }
    }

    /**
     * Getter for the selected CloudTile from {@link GameModel#cloudTiles}.
     *
     * @param cloudTileID Is the ID correspondent to the selected CloudTile.
     * @return The selected CloudTile.
     */
    public CloudTile getCloudTile(int cloudTileID) {
        return cloudTiles.get(cloudTileID);
    }

    /**
     * Draws {@link GameModel#maxNumOfPlayers} + 1 Students from the bag and puts them on the selected CloudTile.
     *
     * @param cloudTile Is the selected CloudTile.
     * @throws TooManyStudentsException If the CloudTile is already full.
     */
    public void moveFromBagToCloudTile(CloudTile cloudTile) throws TooManyStudentsException {
        int num = maxNumOfPlayers + 1;
        try {
            for (int i = 0; i < num; i++) {
                cloudTile.put(bag.pull());
            }
        } catch (EmptyStudentListException e) {
            // No more students will be placed on this cloudTiles
        }
    }

    /**
     * Move MotherNature of the selected number of steps and update her position.
     *
     * @param steps Is the selected number of steps.
     */
    public void moveMotherNature(int steps) {
        //position update in progress()
        motherNature.progress(steps, islandGroups.size());
    }

    /**
     * Join left and right IslandGroups if Possible (the TowerColor is the same)
     * and Updates the IslandGroupID (which is their position in {@link GameModel#islandGroups}).
     *
     * @param position Is the IslandGroupID of the selected IslandGroup to apply {@link GameModel#joinAdjacent(int)} to.
     */
    public void joinAdjacent(int position) {
        int right = (position + 1) % (islandGroups.size());
        int left = (position - 1 + islandGroups.size()) % (islandGroups.size());
        //for(int i=0; i < getRemainingIslandGroups();i++)System.out.println(getIslandGroupByID(i).toString());
        //System.out.println("Position" + position +" - Right" + right + " - Left" + left + "\n");
        //System.out.println("Mother Nature Position" + motherNature.getPosition() + "\n");
        try {
            islandGroups.set(position, this.getIslandGroupByID(position).join(islandGroups.get(right)));
            islandGroups.remove(right);
            if (right < position) {
                position = (position - 1 + islandGroups.size()) % (islandGroups.size());
                left = (left - 1 + islandGroups.size()) % (islandGroups.size());
                //getMotherNature().progress(-1, islandGroups.size()); doesn't work with herald, update moved to updateIslandGroupsID
            } else {
                if (left > position) left = (left - 1 + islandGroups.size()) % (islandGroups.size());
            }
            updateIslandGroupsID();
        } catch (IllegalIslandGroupJoinException | NullIslandGroupException e) {
        }
        //for(int i=0; i < getRemainingIslandGroups();i++)System.out.println(getIslandGroupByID(i).toString());
        //System.out.println("Position" + position +" - Right" + right + " - Left" + left + "\n");
        try {
            islandGroups.set(position, islandGroups.get(position).join(this.getIslandGroupByID(left)));
            islandGroups.remove(left);
            if (left > position) {
                left = (left - 1 + islandGroups.size()) % (islandGroups.size());

            } else {
                position = (position - 1 + islandGroups.size()) % (islandGroups.size());
                left = (left - 1 + islandGroups.size()) % (islandGroups.size());
                if (right > position) right = (right - 1 + islandGroups.size()) % (islandGroups.size());
                //getMotherNature().progress(-1, islandGroups.size()); doesn't work with herald, update moved to updateIslandGroupsID
            }
            updateIslandGroupsID();
        } catch (IllegalIslandGroupJoinException | NullIslandGroupException e) {
        }
        //for(int i=0; i < getRemainingIslandGroups();i++)System.out.println(getIslandGroupByID(i).toString());
        //System.out.println("Position" + position +" - Right" + right + " - Left" + left + "\n");
        //System.out.println("Mother Nature Position" + motherNature.getPosition() + "\n");

    }

    /**
     * Updates the IslandGroupIDs to their respective position in {@link GameModel#islandGroups}.
     */
    private void updateIslandGroupsID() {
        for (int i = 0; i < islandGroups.size(); i++) {
            //Updates mother nature position
            if (islandGroups.get(i).getIslandGroupID() == motherNature.getPosition()) {
                motherNature.setPosition(i);
            }
            islandGroups.get(i).setIslandGroupID(i);
        }
    }

    /**
     * Move all students of a selected CloudTile to the Entrance of a selected Player.
     * Handles EntranceFullException.
     *
     * @param cloudTile Is the selected CloudTile.
     * @param player    Is the selected Player.
     */
    public void moveFromCloudTileToEntrance(CloudTile cloudTile, Player player) {
        try {
            player.getSchoolBoard().addToEntrance(cloudTile.getAndRemoveStudents());
        } catch (EntranceFullException e) {
            Logger.warning("The entrance is already full, can't move more students from cloud tile");
        }
    }

    /**
     * Getter for the attribute {@link GameModel#bag}.
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Getter for the attribute {@link GameModel#unassignedProfessors}.
     *
     * @return The set of Unassigned Professors.
     */
    public Set<Color> getUnassignedProfessors() {
        return unassignedProfessors;
    }

    /**
     * Remove the selected Professor from {@link GameModel#unassignedProfessors}.
     *
     * @param color Is the {@link Color} correspondent to the selected Professor.
     * @return The selected Professor.
     * @throws ProfessorNotFoundException If there is no professor in {@link GameModel#unassignedProfessors} with the given Color.
     */
    public Professor removeProfessor(Color color) throws ProfessorNotFoundException {
        if (!unassignedProfessors.contains(color)) throw new ProfessorNotFoundException();

        unassignedProfessors.remove(color);
        return new Professor(color);
    }

    // Expert mode functions

    /**
     * Calls 3 times the method {@link GameModel#getRandomCharacter()}.
     */
    private void drawThreeCharacters() {
        for (int i = 0; i < 3; i++) characters.add(getRandomCharacter());
    }

    /**
     * Select a random Character that is not already in the list of selected {@link GameModel#characters}.
     *
     * @return The selected Character.
     */
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
            case MUSHROOM_FANATIC -> character.setColor(null);
        }
        return character;
    }

    /**
     * Getter for the randomly selected {@link GameModel#characters}.
     *
     * @return The ArrayList of Characters.
     */
    public List<CharacterCard> getCharacters() {
        return characters;
    }

    /**
     * Getter of the selected Character from {@link GameModel#characters}.
     *
     * @param id Is the ID correspondent to the selected Character.
     * @return The selected Character.
     * @throws CharacterCardNotFound If there is no Character in {@link GameModel#characters} with the given ID.
     */
    public CharacterCard getCharacterById(int id) throws CharacterCardNotFound {
        if (id < 0 || id >= 3)
            throw new CharacterCardNotFound();
        CharacterCard ret = characters.get(id);
        return ret;
    }

    /**
     * Getter of the selected Character from {@link GameModel#characters}.
     *
     * @param characterType Is the {@link CharacterType} correspondent to the selected Character.
     * @return The selected Character.
     * @throws CharacterCardNotFound If there is no Character in {@link GameModel#characters} with the given {@link CharacterType}.
     */
    public CharacterCard getCharacterByType(CharacterType characterType) {
        for (CharacterCard characterCard : characters) {
            if (characterCard.getCharacter().equals(characterType)) {
                return characterCard;
            }
        }
        return null;
    }

    /**
     * Sets the attribute {@link GameModel#activeCharacterEffect} to the {@link CharacterType}
     * of the active Character (null if none is active).
     *
     * @param type Is the {@link CharacterType} correspondent to the active Character.
     */
    public void setActiveCharacterEffect(CharacterType type) {
        activeCharacterEffect = type;
    }

    /**
     * Getter for the attribute {@link GameModel#activeCharacterEffect}.
     */
    public CharacterType getActiveCharacterEffect() {
        return activeCharacterEffect;
    }

    /**
     * Getter for the size of {@link GameModel#islandGroups}.
     *
     * @return The number of remaining IslandGroups.
     */
    public int getRemainingIslandGroups() {
        return islandGroups.size();
    }

    /**
     * Returns True if there is no more Rooks in ANY of the Players SchoolBoards, otherwise False.
     */
    public boolean checkForRooksEmpty() {
        for (int i = 0; i < getPlayers().size(); i++) {
            try {
                if (getPlayerByID(i).getSchoolBoard().getTowers().size() == 0) {
                    return true;
                }
            } catch (PlayerNotFoundException e) {
                Logger.warning("Trying to check remaining rooks for an invalid player");
            }
        }
        return false;
    }

    /**
     * Calls {@link GameModel#getRemainingIslandGroups()}.
     * Returns True if the result is <=3, otherwise False.
     */
    public boolean checkForTooFewIslands() {
        return getRemainingIslandGroups() <= 3;
    }

    /**
     * Getter for {@link GameModel#islandGroups}.
     *
     * @return a List.
     */
    public List<IslandGroup> getIslandGroups() {
        return islandGroups;
    }

    /**
     * Getter for {@link GameModel#cloudTiles}.
     *
     * @return a List.
     */
    public List<CloudTile> getCloudTiles() {
        return cloudTiles;
    }
}