package network;

import model.board.Assistant;
import model.board.CloudTile;
import model.board.IslandGroup;
import model.board.SchoolBoard;
import model.expert.CharacterCard;
import util.CharacterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightModel {
    private String playerName;
    private Map<String, SchoolBoard> schoolBoardMap;
    private List<CloudTile> cloudTiles;
    private List<IslandGroup> islandGroups;

    private List<Assistant> deck;

    private List<CharacterCard> characters;
    private CharacterType activeCharacterEffect;

    public LightModel(String playerName) {
        this.playerName = playerName;
        this.schoolBoardMap = new HashMap<>();
        this.cloudTiles = null;
        this.islandGroups = null;
        this.deck = null;

        this.characters = null;
        this.activeCharacterEffect = null;
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

    public void setDeck(List<Assistant> updatedDeck) {
        this.deck = updatedDeck;
    }

    public void setCharacters(List<CharacterCard> characterCards) {
        this.characters = characterCards;
    }

    public void setActiveCharacterEffect(CharacterType activeCharacterEffect) {
        this.activeCharacterEffect = activeCharacterEffect;
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
}
