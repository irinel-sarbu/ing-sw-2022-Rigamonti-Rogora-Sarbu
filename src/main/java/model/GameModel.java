package model;

import observer.Observable;
import exceptions.*;
import model.board.*;
import model.expert.*;
import util.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameModel extends Observable {
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
     * Constructor of the GameModel
     *
     * @param maxNumOfPlayers Number of players chosen when the game is created. Can be 2 or 3.
     * @param gameMode        GameMode chosen when the game is created. Can be EXPERT or NORMAL.
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

    public CoinSupply getCoinSupply() {
        return coinSupply;
    }

    public MotherNature getMotherNature() {
        return motherNature;
    }

    public int getMaxNumOfPlayers() {
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

    public int getPlayerId(Player player) {
        return players.indexOf(player);
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

    public Player getPlayerByName(String name) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }

        throw new PlayerNotFoundException("Player " + name + " not found!");
    }

    public void addPlayer(Player player) {
        players.add(player);
        moveFromBagToEntrance(player);
        player.getSchoolBoard().setUpTowers(player.getColor(), maxNumOfPlayers);
    }

    public boolean removePlayerByName(String name) {
        Player player;
        try {
            player = getPlayerByName(name);
        } catch (PlayerNotFoundException e) {
            player = null;
            Logger.warning(e.getMessage());
        }

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
                    Logger.warning("Pulling from initial bag. Something went wrong...");
                }
                islandGroup.getIslands().get(0).addStudent(student);
            }
            //System.out.println(islandGroup);
        }
    }

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

    public IslandGroup getIslandGroupByID(int id) throws IslandGroupNotFoundException {
        if (islandGroups.get(id) == null) throw new IslandGroupNotFoundException();
        return islandGroups.get(id);
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
        try {
            moveFromBagToCloudTile(cloudTiles.get(cloudTileID));
        } catch (TooManyStudentsException e) {
            // do Nothing
        }
    }

    public CloudTile getCloudTile(int cloudTileID) {
        return cloudTiles.get(cloudTileID);
    }

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

    public void moveMotherNature(int steps) {
        motherNature.progress(steps, islandGroups.size());
    }

    public void joinAdjacent(int position) {
        int left = (position - 1 + islandGroups.size() - 1) % (islandGroups.size() - 1);
        int right = (position + 1) % (islandGroups.size() - 1);
        try {
            islandGroups.set(position, this.getIslandGroupByID(position).join(islandGroups.get(right)));
            islandGroups.remove(right);
        } catch (IllegalIslandGroupJoinException | NullIslandGroupException e) {
        }
        try {
            islandGroups.set(left, islandGroups.get(left).join(this.getIslandGroupByID(position)));
            islandGroups.remove(position);
            getMotherNature().progress(-1, islandGroups.size());
        } catch (IllegalIslandGroupJoinException | NullIslandGroupException e) {
        }
        updateIslandGroupsID();
    }

    private void updateIslandGroupsID() {
        for (int i = 0; i < islandGroups.size(); i++) {
            islandGroups.get(i).setIslandGroupID(i);
        }
    }

    public void moveFromCloudTileToEntrance(CloudTile cloudTile, Player player) {
        try {
            player.getSchoolBoard().addToEntrance(cloudTile.getAndRemoveStudents());
        } catch (EntranceFullException e) {
            e.printStackTrace();
        }
    }

    public Bag getBag() {
        return bag;
    }

    public Set<Color> getUnassignedProfessors() {
        return unassignedProfessors;
    }

    public Professor removeProfessor(Color color) throws ProfessorNotFoundException {
        if (!unassignedProfessors.contains(color)) throw new ProfessorNotFoundException();

        unassignedProfessors.remove(color);
        return new Professor(color);
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
            case MUSHROOM_FANATIC -> character.setColor(null);
        }
        return character;
    }

    public List<CharacterCard> getCharacters() {
        return characters;
    }

    public CharacterCard getCharacterById(int id) throws CharacterCardNotFound {
        CharacterCard ret = characters.get(id);
        if (ret == null)
            throw new CharacterCardNotFound();
        return ret;
    }

    public CharacterCard getCharacterByType(CharacterType characterType) {
        for (CharacterCard characterCard : characters) {
            if (characterCard.getCharacter().equals(characterType)) {
                return characterCard;
            }
        }
        return null;
    }

    public void setActiveCharacterEffect(CharacterType type) {
        activeCharacterEffect = type;
    }

    public CharacterType getActiveCharacterEffect() {
        return activeCharacterEffect;
    }

    public int getRemainingIslandGroups() {
        return islandGroups.size();
    }

    public boolean checkForRooksEmpty() {
        for (int i = 0; i < getPlayers().size(); i++) {
            try {
                if (getPlayerByID(i).getSchoolBoard().getTowers().size() == 0) {
                    return true;
                }
            } catch (PlayerNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean checkForToFewIslands() {
        return getRemainingIslandGroups() <= 3;
    }
}