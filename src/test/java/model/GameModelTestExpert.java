package model;

import exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Color;
import util.GameMode;
import util.TowerColor;
import util.Wizard;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTestExpert {
    private static GameModel game;

    @BeforeAll
    public static void setUp() {
        game = new GameModel(3, GameMode.EXPERT);

        game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK));
        game.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE));
    }

    /*
    Testing getters (should not return null or 0 in case of int)
    */
    @Test
    public void getCoinSupply() {
        assertNotNull(game.getCoinSupply());
    }

    @Test
    public void getMotherNature() {
        assertNotNull(game.getMotherNature());
    }

    @Test
    public void getNumOfPlayers() {
        assertTrue(game.getPlayerSize() != 0);
    }

    @Test
    public void getGameMode() {
        assertTrue(game.getGameMode() != null);
    }

    @Test
    public void getPlayerByID() {
        try {
            assertNotNull(game.getPlayerByID(1));
            assertEquals("pietro", game.getPlayerByID(1).getName());
            assertEquals(game.getPlayerByID(1).getColor(), TowerColor.WHITE);
            System.out.println("------------->Player 2 is pietro");
        } catch (PlayerNotFoundException e) {
            fail();
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
            assertEquals(1, game.getPlayerId(game.getPlayerByID(1)));
        } catch (PlayerNotFoundException e) {
            fail();
        }
        System.out.println("------------->ID return is correct");
    }

    @Test
    public void getPlayers() {
        assertNotNull(game.getPlayers());
    }

    @Test
    public void getPlayerByName() {
        try {
            assertEquals(game.getPlayerByName("marco"), game.getPlayerByID(0));
        } catch (PlayerNotFoundException e) {
            fail();
        }
        System.out.println("------------->Player 1 marco found");
    }

    @Test
    public void getIslandGroupByID() {
        assertNotNull(game.getIslandGroupByID(0));
    }

    @Test
    public void getIslandTileByID() {
        assertTrue(game.getIslandGroupByID(0).getSize() != 0);
        assertNotNull(game.getIslandTileByID(0));
    }

    @Test
    public void getNumOfCloudTiles() {
        assertEquals(3, game.getNumOfCloudTiles());
    }

    @Test
    public void getCloudTile() {
        assertNotNull(game.getCloudTile(0));
    }

    @Test
    public void getBag() {
        assertNotNull(game.getBag());
    }

    @Test
    public void getUnassignedProfessor() {
        assertNotNull(game.getUnassignedProfessors());
        //atGameStart the size should be 5
        assertEquals(5, game.getUnassignedProfessors().size());
    }

    @Test
    public void getCharacters() {
        assertNotNull(game.getCharacters());
        assertEquals(3, game.getCharacters().size());
    }

    @Test
    public void getCharacterByID() {
        try {
            assertNotNull(game.getCharacterById(0));
        } catch (CharacterCardNotFound e) {
            fail();
        }
    }

    @Test
    public void getActualCharacters() {
        assertNotNull(game.getCharacters());
        System.out.println("------------->Characters:");
        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("Character " + i + ": " + game.getCharacterById(i).getCharacter().toString());
                assertNotNull(game.getCharacterByType(game.getCharacterById(i).getCharacter()));
            } catch (CharacterCardNotFound e) {
                fail();
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

        game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK));
        game.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE));

    }

    @Test
    public void addPlayer() {

        game.addPlayer(new Player("giacomo", Wizard.WIZARD_4, TowerColor.GRAY));
        try {
            assertNotNull(game.getPlayerByName("giacomo"));
        } catch (PlayerNotFoundException e) {
            fail();
        }
        System.out.println("------------->Added and recovered giacomo");
    }

    @Test
    public void removePlayerByName() {
        game.removePlayerByName("marco");
        try {
            assertNotNull(game.getPlayerByID(0));
            assertEquals("pietro", game.getPlayerByID(0).getName());
        } catch (PlayerNotFoundException e) {
            fail();
        }
        System.out.println("------------->Removed marco successfully");
    }

    @Test
    public void moveFromBagToIslandTile() {
        for (int i = 0; i < 12; i++) {
            if (i != game.getMotherNature().getPosition() && i != (game.getMotherNature().getPosition() + 6) % 12) {
                assertTrue(game.getIslandTileByID(i).getStudentsNumber(Color.YELLOW) != 0 ||
                        game.getIslandTileByID(i).getStudentsNumber(Color.RED) != 0 ||
                        game.getIslandTileByID(i).getStudentsNumber(Color.GREEN) != 0 ||
                        game.getIslandTileByID(i).getStudentsNumber(Color.BLUE) != 0 ||
                        game.getIslandTileByID(i).getStudentsNumber(Color.PINK) != 0
                );
            }
        }
    }

    @Test
    public void moveFromBagToEntrance() {
        for (Player player : game.getPlayers()) {
            assertTrue(player.getSchoolBoard().getEntranceStudents().size() != 0);
        }
    }

    @Test
    public void refillCloudTiles() {
        for (int i = 0; i < 3; i++) {
            assertTrue(game.getCloudTile(i).getStudents().size() != 0);
        }
    }

    @Test
    public void moveFromBagToCloudTile() {
        //it's called by RefillCloudTiles, so the test is the same
        for (int i = 0; i < 3; i++) {
            assertTrue(game.getCloudTile(i).getStudents().size() != 0);
        }
    }

    @Test
    public void joinAdjacent() {
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(1).setTowerColor(TowerColor.BLACK);
        //join should now join islandGroup 0 and 1, and in the new 0 there should be Tile 0 and 1
        //let's assume mother nature is on 0, and just turned the rook on 0 black
        game.joinAdjacent(0);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        System.out.println("");
        assertEquals(11, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(0).getIslands().size());
        //now testing the right join
        game.getIslandTileByID(5).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(6).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(5);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        System.out.println("");
        assertEquals(10, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(4).getIslands().size());
        //now testing both joins
        game.getIslandTileByID(7).setTowerColor(TowerColor.WHITE);
        game.getIslandTileByID(8).setTowerColor(TowerColor.WHITE);
        game.getIslandTileByID(9).setTowerColor(TowerColor.WHITE);
        game.joinAdjacent(6);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        assertEquals(8, game.getRemainingIslandGroups());
        assertEquals(3, game.getIslandGroupByID(5).getIslands().size());
        System.out.println("------------->Join done successfully");
    }

    @Test
    public void moveFromCloudTileToEntrance() {
        try {
            for (int i = 0; i < 3; i++)
                game.getPlayerByID(0).getSchoolBoard().removeFromEntrance(game.getPlayerByID(0).getSchoolBoard().getEntranceStudents().get(0).getID());
            int size = game.getPlayerByID(0).getSchoolBoard().getEntranceStudents().size();
            game.moveFromCloudTileToEntrance(game.getCloudTile(0), game.getPlayerByID(0));
            //number of students in the entrance must increase by 4 if players are 3
            assertEquals(game.getPlayerByID(0).getSchoolBoard().getEntranceStudents().size(), (size + 4));
        } catch (PlayerNotFoundException | StudentNotFoundException e) {
            fail();
        }
        System.out.println("------------->Moving students cloud->entrance done successfully");
    }

    @Test
    public void removeProfessor() {
        int size = game.getUnassignedProfessors().size();
        try {
            game.removeProfessor(Color.BLUE);
        } catch (ProfessorNotFoundException e) {
            fail();
        }
        assertTrue(size!=game.getUnassignedProfessors().size());
        assertEquals(4, game.getUnassignedProfessors().size());
    }

    //DrawThreeCharacters and getRandomCharacter already tested

    @Test
    public void checkForTooFewIsland() {
        assertFalse(game.checkForToFewIslands());
    }

    @Test
    public void checkForRooksEmpty() {
        assertFalse(game.checkForRooksEmpty());
    }
}
