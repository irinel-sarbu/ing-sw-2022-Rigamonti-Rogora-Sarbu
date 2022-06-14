package network;

import model.board.Assistant;
import model.board.CloudTile;
import model.board.IslandGroup;
import model.board.SchoolBoard;
import model.expert.CharacterCard;
import util.CharacterType;
import util.GameMode;
import util.GameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model stored on client. Updated by client controller by events coming from server.
 */
public class LightModel {
    private String playerName, currentPlayerName;
    private Map<String, SchoolBoard> schoolBoardMap;
    private List<CloudTile> cloudTiles;
    private List<IslandGroup> islandGroups;
    private int motherNaturePosition;

    private List<Assistant> deck;
    private Assistant chosenAssistant;

    private List<CharacterCard> characters;
    private CharacterType activeCharacterEffect;
    private GameState gameState;

    private Boolean lastRound;

    /**
     * Default constructor
     *
     * @param playerName owner
     */
    public LightModel(String playerName) {
        this.playerName = playerName;
        this.schoolBoardMap = new HashMap<>();
        this.cloudTiles = null;
        this.islandGroups = null;
        this.deck = null;
        this.chosenAssistant = null;
        this.gameState = GameState.SETUP;
        this.characters = null;
        this.activeCharacterEffect = null;
        this.lastRound = false;
    }

    public void setPlayerSchoolBoard(String playerName, SchoolBoard updatedSchoolBoard) {
        this.schoolBoardMap.put(playerName, updatedSchoolBoard);
    }

    public void setCloudTiles(List<CloudTile> updatedCloudTiles) {
        this.cloudTiles = updatedCloudTiles;
    }

    public void setIslandGroups(List<IslandGroup> updatedIslandGroups) {
        this.islandGroups = updatedIslandGroups;
    }

    public void setMotherNaturePosition(int updatedPosition) {
        this.motherNaturePosition = updatedPosition;
    }

    public void setDeck(List<Assistant> updatedDeck) {
        this.deck = updatedDeck;
    }

    public void setCharacters(List<CharacterCard> characterCards) {
        this.characters = characterCards;
    }

    public void setActiveCharacterEffect(CharacterType activeCharacterEffect) {
        this.activeCharacterEffect = activeCharacterEffect;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setLastRound(Boolean lastRound) {
        this.lastRound = lastRound;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public void setChosenAssistant(Assistant chosenAssistant) {
        this.chosenAssistant = chosenAssistant;
    }

    public Map<String, SchoolBoard> getSchoolBoardMap() {
        return schoolBoardMap;
    }

    public List<CloudTile> getCloudTiles() {
        return cloudTiles;
    }

    public List<IslandGroup> getIslandGroups() {
        return islandGroups;
    }

    public List<Assistant> getDeck() {
        return deck;
    }

    public List<CharacterCard> getCharacters() {
        return characters;
    }

    public CharacterType getActiveCharacterEffect() {
        return activeCharacterEffect;
    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public GameMode getGameMode() {
        if (characters == null) return GameMode.NORMAL;
        else return GameMode.EXPERT;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Boolean isLastRound() {
        return lastRound;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public Assistant getChosenAssistant() {
        return chosenAssistant;
    }
}
