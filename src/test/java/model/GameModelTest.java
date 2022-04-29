package model;

import exceptions.*;
import model.board.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    private static GameModel game;
    private static GameModel normalGame;

    @BeforeAll
    public static void setup() {
        game = new GameModel(3, GameMode.EXPERT);

        game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK, game.getGameMode()));
        game.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE, game.getGameMode()));
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
        game.moveMotherNature(3);
        assertNotNull(game.getMotherNature());
    }

    @Test
    public void getNumOfPlayers() {
        assertTrue(game.getPlayerSize() != 0);
        assertEquals(3, game.getMaxNumOfPlayers());
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
        assertThrows(PlayerNotFoundException.class, () -> game.getPlayerByID(3));
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
        assertThrows(PlayerNotFoundException.class, () -> game.getPlayerByName("giorgio"));
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
        assertThrows(IslandNotFoundException.class, () -> game.getIslandTileByID(13));
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
        assertThrows(CharacterCardNotFound.class, () -> game.getCharacterById(4));
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
        try {
            assertNotNull(game.getCharacterByType(game.getCharacterById(0).getCharacter()));
        } catch (CharacterCardNotFound e) {
            fail();
        }
        for (CharacterType type : CharacterType.values()) {
            CharacterType typeTester1 = null, typeTester2 = null, typeTester3 = null;
            try {
                typeTester1 = game.getCharacterById(0).getCharacter();
                typeTester2 = game.getCharacterById(1).getCharacter();
                typeTester3 = game.getCharacterById(2).getCharacter();
            } catch (CharacterCardNotFound e) {
                fail();
            }
            if (type != typeTester1 && type != typeTester2 && type != typeTester3)
                assertNull(game.getCharacterByType(type));
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

        game.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK, game.getGameMode()));
        game.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE, game.getGameMode()));

    }

    @Test
    public void addPlayer() {

        game.addPlayer(new Player("giacomo", Wizard.WIZARD_4, TowerColor.GRAY, game.getGameMode()));
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
        System.out.println("------------->Trying to remove a non existing player: ");
        game.removePlayerByName("giorgio");
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
    public void testCloudTiles() {
        for (int i = 0; i < 3; i++) {
            assertTrue(game.getCloudTile(i).getStudents().size() != 0);
        }
    }

    @Test
    public void joinAdjacent() {
        game.getIslandTileByID(1).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(2).setTowerColor(TowerColor.BLACK);
        //join should now join islandGroup 1 and 2, and in the new 1 there should be Tile 1 and 2
        //let's assume mother nature is on 1, and just turned the rook on 1 black
        game.joinAdjacent(1);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(11, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(1).getIslands().size());
        //now testing the left join
        game.getIslandTileByID(5).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(6).setTowerColor(TowerColor.BLACK);
        //5 cause position got -1 for the previous join
        game.joinAdjacent(5);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(10, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(4).getIslands().size());
        //now testing both joins
        game.getIslandTileByID(7).setTowerColor(TowerColor.WHITE);
        game.getIslandTileByID(8).setTowerColor(TowerColor.WHITE);
        game.getIslandTileByID(9).setTowerColor(TowerColor.WHITE);
        //2 cause position got -2 for the previous joins
        game.joinAdjacent(6);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(8, game.getRemainingIslandGroups());
        assertEquals(3, game.getIslandGroupByID(5).getIslands().size());
        System.out.println("------------->Join done successfully");
    }

    @Test
    public void joinAdjacentSpecialZeroLeft() {
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(11).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(0);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(11, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(0).getIslands().size());

        System.out.println("------------->Special Join done successfully");
    }

    @Test
    public void joinAdjacentSpecialZeroRight() {
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(1).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(0);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(11, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(0).getIslands().size());

        System.out.println("------------->Special Join done successfully");
    }

    @Test
    public void joinAdjacentSpecialZeroBoth() {
        game.getIslandTileByID(11).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(1).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(0);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(10, game.getRemainingIslandGroups());
        assertEquals(3, game.getIslandGroupByID(0).getIslands().size());

        System.out.println("------------->Special Join done successfully");
    }

    @Test
    public void joinAdjacentSpecial11Left() {
        game.getIslandTileByID(10).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(11).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(11);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(11, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(10).getIslands().size());

        System.out.println("------------->Special Join done successfully");
    }

    @Test
    public void joinAdjacentSpecial11Right() {
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(11).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(11);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(11, game.getRemainingIslandGroups());
        assertEquals(2, game.getIslandGroupByID(10).getIslands().size());

        System.out.println("------------->Special Join done successfully");
    }

    @Test
    public void joinAdjacentSpecial11Both() {
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(10).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(11).setTowerColor(TowerColor.BLACK);
        game.joinAdjacent(11);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(10, game.getRemainingIslandGroups());
        assertEquals(3, game.getIslandGroupByID(9).getIslands().size());

        System.out.println("------------->Special Join done successfully");
    }

    @Test
    public void joinAdjacentSpecialNoJoin() {
        game.joinAdjacent(5);
        game.joinAdjacent(0);
        game.getIslandTileByID(0).setTowerColor(TowerColor.BLACK);
        game.getIslandTileByID(10).setTowerColor(TowerColor.WHITE);
        game.getIslandTileByID(11).setTowerColor(TowerColor.GRAY);
        game.joinAdjacent(11);
        //for(int i=0; i < game.getRemainingIslandGroups();i++)System.out.println(game.getIslandGroupByID(i).toString());
        //System.out.println("");
        assertEquals(12, game.getRemainingIslandGroups());
        assertEquals(1, game.getIslandGroupByID(11).getIslands().size());

        System.out.println("------------->Special Join done successfully");
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

        try {
            System.out.println("------------->Trying to move to an already full entrance");
            game.moveFromCloudTileToEntrance(game.getCloudTile(2), game.getPlayerByID(0));
        } catch (PlayerNotFoundException e) {
            fail();
        }
    }

    @Test
    public void removeProfessor() {
        int size = game.getUnassignedProfessors().size();
        try {
            game.removeProfessor(Color.BLUE);
        } catch (ProfessorNotFoundException e) {
            fail();
        }
        assertTrue(size != game.getUnassignedProfessors().size());
        assertEquals(4, game.getUnassignedProfessors().size());
        assertThrows(ProfessorNotFoundException.class, () -> game.removeProfessor(Color.BLUE));
    }

    //DrawThreeCharacters and getRandomCharacter already tested

    @Test
    public void checkForTooFewIsland() {
        assertFalse(game.checkForToFewIslands());
    }

    @Test
    public void checkForRooksEmpty() {
        assertFalse(game.checkForRooksEmpty());
        try {
            for (int i = 0; i < 6; i++) game.getPlayerByID(0).getSchoolBoard().removeTower();
        } catch (PlayerNotFoundException | TowersIsEmptyException e) {
            fail();
        }
        assertTrue(game.checkForRooksEmpty());
    }

    @Test
    public void testCharacterEffect() {
        assertNull(game.getActiveCharacterEffect());
        game.setActiveCharacterEffect(CharacterType.CENTAUR);
        assertSame(game.getActiveCharacterEffect(), CharacterType.CENTAUR);
    }

    @Test
    public void testNormal() {
        normalGame = new GameModel(3, GameMode.NORMAL);
        normalGame.addPlayer(new Player("marco", Wizard.WIZARD_1, TowerColor.BLACK, game.getGameMode()));
        normalGame.addPlayer(new Player("pietro", Wizard.WIZARD_2, TowerColor.WHITE, game.getGameMode()));
    }

    @Test
    public void refillCloudTiles() {
        game.refillCloudTile(0);
        int rem = game.getBag().getRemainingStudents();
        for (int i = 0; i < rem; i++) game.getBag().pull();
        try {
            game.moveFromBagToCloudTile(game.getCloudTile(0));
        } catch (TooManyStudentsException e) {
            fail();
        }
    }

    @Test
    public void additionalGetters() {
        assertNotNull(game.getIslandGroups());
        assertNotNull(game.getCloudTiles());
    }

}