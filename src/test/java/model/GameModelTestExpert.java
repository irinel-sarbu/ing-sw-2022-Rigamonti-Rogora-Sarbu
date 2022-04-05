package model;

import exceptions.CharacterCardNotFound;
import exceptions.MaxPlayersException;
import exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.GameMode;
import util.TowerColor;
import util.Wizard;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameModelTestExpert {
    private static GameModel game;

    @BeforeAll
    public static void setUp() {
        game = new GameModel(3, GameMode.EXPERT);
        try {
            game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK));
            game.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE));
        } catch (MaxPlayersException e) {
            assertTrue(false);
        }
    }

    /*
    Testing getters (should not return null or 0 in case of int)
    */
    @Test
    public void getCoinSupply() {
        assertTrue(game.getCoinSupply() != null);
    }

    @Test
    public void getMotherNature() {
        assertTrue(game.getMotherNature() != null);
    }

    @Test
    public void getNumOfPlayers() {
        assertTrue(game.getNumOfPlayers() != 0);
    }

    @Test
    public void getGameMode() {
        assertTrue(game.getGameMode() != null);
    }

    @Test
    public void getPlayerByID() {
        try {
            assertTrue(game.getPlayerByID(1) != null);
            assertTrue(game.getPlayerByID(1).getName().equals("pietro"));
            assertTrue(game.getPlayerByID(1).getColor().equals(TowerColor.WHITE));
            System.out.println("------------->Player 2 is pietro");
        } catch (PlayerNotFoundException e) {
            assertTrue(false);
        }
    }

    @Test
    public void getPlayerNames() {
        assertTrue(game.getPlayerNames().get(0).equals("marco") && game.getPlayerNames().get(1).equals("pietro"));
        System.out.println("------------->Names are correct");
    }

    @Test
    public void getPlayerID() {
        try {
            assertTrue(game.getPlayerId(game.getPlayerByID(1)) == 1);
        } catch (PlayerNotFoundException e) {
            assertTrue(false);
        }
        System.out.println("------------->ID return is correct");
    }

    @Test
    public void getPlayers() {
        assertTrue(game.getPlayers() != null);
    }

    @Test
    public void getPlayerByName() {
        try {
            assertTrue(game.getPlayerByName("marco").equals(game.getPlayerByID(0)));
        } catch (PlayerNotFoundException e) {
            assertTrue(false);
        }
        System.out.println("------------->Player 1 marco found");
    }

    @Test
    public void getIslandGroupByID() {
        assertTrue(game.getIslandGroupByID(0) != null);
    }

    @Test
    public void getIslandTileByID() {
        assertTrue(game.getIslandGroupByID(0).getSize() != 0);
        assertTrue(game.getIslandTileByID(0) != null);
    }

    @Test
    public void getNumOfCloudTiles() {
        assertTrue(game.getNumOfCloudTiles() == 3);
    }

    @Test
    public void getCloudTile() {
        assertTrue(game.getCloudTile(0) != null);
    }

    @Test
    public void getBag() {
        assertTrue(game.getBag() != null);
    }

    @Test
    public void getUnassignedProfessor() {
        assertTrue(game.getUnassignedProfessors() != null);
        //atGameStart the size should be 5
        assertTrue(game.getUnassignedProfessors().size() == 5);
    }

    @Test
    public void getCharacters() {
        assertTrue(game.getCharacters() != null);
        assertTrue(game.getCharacters().size() == 3);
    }

    @Test
    public void getCharacterByID() {
        try {
            assertTrue(game.getCharacterById(0) != null);
        } catch (CharacterCardNotFound e) {
            assertTrue(false);
        }
    }

    @Test
    public void getActualCharacters() {
        assertTrue(game.getCharacters() != null);
        System.out.println("------------->Characters:");
        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("Character " + i + ": " + game.getCharacterById(i).getCharacter().toString());
                assertTrue(game.getCharacterByType(game.getCharacterById(i).getCharacter()) != null);
            } catch (CharacterCardNotFound e) {
                assertTrue(false);
            }
        }
    }

    @Test
    public void getRemainingIslandGroups() {
        //at the start of the game, the size shouldn't be 0
        assertTrue(game.getRemainingIslandGroups() != 0);
    }


    /*
    Testing other methods (add, set, other functions. Should all not create any exception, Should be recovered with get methods)
     */

    //needed to maintain the initial testing state, modifiable with setters and other methods
    @AfterEach
    public void resetModel() {
        game = new GameModel(3, GameMode.EXPERT);
        try {
            game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK));
            game.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE));
        } catch (MaxPlayersException e) {
            assertTrue(false);
        }
    }

    @Test
    public void addPlayer() {
        try {
            game.addPlayer(new Player("giacomo", Wizard.WIZARD_4, TowerColor.GRAY));
        } catch (MaxPlayersException e) {
            assertTrue(false);
        }
        try {
            assertTrue(game.getPlayerByName("giacomo") != null);
        } catch (PlayerNotFoundException e) {
            assertTrue(false);
        }
        System.out.println("------------->Added and recovered giacomo");
    }


}
