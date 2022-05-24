package controller.server.states;

import controller.server.GameLobby;
import exceptions.EntranceFullException;
import exceptions.ProfessorFullException;
import exceptions.StudentNotFoundException;
import model.GameModel;
import model.Player;
import model.board.Professor;
import model.board.Student;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResolveIslandTest {
    private static ResolveIsland resolveIsland;
    private static StudentMovement studentMovement;
    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> player;

    public int firstOf(int i) {
        return player.get(i).getSchoolBoard().getEntranceStudents().get(0).getID();
    }

    private void print() {
//        System.out.println(IslandGroup.allToString(gameModel.getIslandGroups()) + "\n" + SchoolBoard.allToString(gameModel.getPlayers().stream().map(Player::getSchoolBoard).toList()));
    }

    @BeforeEach
    public void ResolveIsland() {
        Random.setSeed(0);

        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server(5000));

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.RESOLVE_ISLAND);
        player = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(player.get(0));
        resolveIsland = new DefaultResolveIsland();

        // empty player 0 entrance
        try {
            for (int i = 0; i < 10; i++) {
                player.get(0).getSchoolBoard().removeFromEntrance(firstOf(0));
                player.get(1).getSchoolBoard().removeFromEntrance(firstOf(1));
            }
        } catch (IndexOutOfBoundsException e) {
            try {
                // fill player 0 entrance with GREEN students only
                int id0 = 1000,
                        id1 = 2000;
                for (int i = 0; i < 10; i++) {
                    player.get(0).getSchoolBoard().addToEntrance(new Student(id0++, Color.GREEN));
                    player.get(1).getSchoolBoard().addToEntrance(new Student(id1++, Color.RED));
                }
            } catch (EntranceFullException ex) {
                // do nothing
            }
        } catch (StudentNotFoundException e) {
            System.err.println(e + " not expected");
            fail();
        }
        studentMovement = new DefaultStudentMovement();
    }

    @Test
    public void solveIsland() {
        resolveIsland = new DefaultResolveIsland();
        gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
        int ID;

        for (Player p : player) {
            assertEquals(6, p.getSchoolBoard().getTowers().size());
        }

        try {
            assertNull(gameModel.getIslandGroupByID(1).getTowersColor());
            studentMovement.moveStudentToDining(gameLobby, player.get(0), firstOf(0));
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), 1);
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            resolveIsland.solveIsland(gameLobby, 1);
            assertEquals(gameModel.getPlayerByID(0).getColor(), gameModel.getIslandTileByID(1).getTowerColor());
            assertEquals(5, gameModel.getPlayerByID(0).getSchoolBoard().getTowers().size());

            // check that a new resolve does not destroy old towers
            resolveIsland.solveIsland(gameLobby, 1);
            assertEquals(5, gameModel.getPlayerByID(0).getSchoolBoard().getTowers().size());

            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), 2);
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            resolveIsland.solveIsland(gameLobby, 2);
            assertEquals(11, gameModel.getIslandGroups().size());

            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), 3);
            resolveIsland.solveIsland(gameLobby, 2);
            assertEquals(3, player.get(0).getSchoolBoard().getTowers().size());

            // check that, even with multiple towers the towers are not destroyed
            resolveIsland.solveIsland(gameLobby, 2);
            assertEquals(3, player.get(0).getSchoolBoard().getTowers().size());

            assertEquals(10, gameModel.getIslandGroups().size());

            // checking correct towers switch

            // give red professor to player 1
            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            gameLobby.setCurrentPlayer(player.get(1));
            ID = player.get(1).getSchoolBoard().getEntranceStudents().get(0).getID();
            studentMovement.moveStudentToDining(gameLobby, player.get(1), ID);
            // ensure player has 9 students in entrance


            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            gameLobby.setCurrentPlayer(player.get(1));
            int id = 4123;
            for (int i = 0; i < 3; i++) {
                for (int j = 1; j <= 3; j++) {
                    ID = player.get(1).getSchoolBoard().getEntranceStudents().get(0).getID();
                    studentMovement.moveStudentToIsland(gameLobby, player.get(1), ID, j);

                    // unlock number of movable students
                    gameLobby.resetStudentsMoved();
                    player.get(1).getSchoolBoard().addToEntrance(new Student(id++, Color.RED));
                }
            }
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);

            resolveIsland.solveIsland(gameLobby, 1);
            assertEquals(TowerColor.BLACK, gameModel.getIslandGroupByID(1).getTowersColor());

            assertEquals(3, player.get(1).getSchoolBoard().getTowers().size());
            assertEquals(6, player.get(0).getSchoolBoard().getTowers().size());


        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }
    }

    @Test
    public void centaurResolveIsland() {
        resolveIsland = new CentaurResolveIsland();

        gameModel.getIslandGroupByID(1).setTowersColor(player.get(1).getColor());

        try {
            player.get(0).getSchoolBoard().addProfessor(gameModel.removeProfessor(Color.GREEN));
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            gameModel.getIslandTileByID(1).setTowerColor(TowerColor.BLACK);
            resolveIsland.solveIsland(gameLobby, 1);

            assertEquals(player.get(0).getColor(), gameModel.getIslandGroupByID(1).getTowersColor());

            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            gameLobby.setCurrentPlayer(player.get(0));
            gameModel.getIslandGroupByID(1).getIslands().get(0).addStudent(new Student(3000, Color.GREEN));
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            resolveIsland.solveIsland(gameLobby, 1);
            assertEquals(player.get(0).getColor(), gameModel.getIslandGroupByID(1).getTowersColor());

        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }
    }

    @Test
    public void knightResolveIsland() {
        resolveIsland = new KnightResolveIsland();

        try {
            player.get(0).getSchoolBoard().addProfessor(gameModel.removeProfessor(Color.GREEN));
            player.get(1).getSchoolBoard().addProfessor(gameModel.removeProfessor(Color.RED));
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);

            gameModel.getIslandTileByID(1).addStudent(new Student(4000, Color.GREEN));
            gameModel.getIslandTileByID(1).addStudent(new Student(4001, Color.RED));

            gameLobby.setCurrentPlayer(player.get(1));
            resolveIsland.solveIsland(gameLobby, 1);

            assertEquals(player.get(1).getColor(), gameModel.getIslandTileByID(1).getTowerColor());

        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }
    }

    @Test
    public void MushroomFanaticResolveIsland() {

        // set resolve method
        resolveIsland = new MushroomFanaticResolveIsland();

        // set mushroom fanatic color
        resolveIsland = new MushroomFanaticResolveIsland();
        assertNotNull(gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC));
        gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).setColor(Color.RED);

        // assign profesors
        try {
            player.get(0).getSchoolBoard().addProfessor(new Professor(Color.GREEN));
            player.get(0).getSchoolBoard().addProfessor(new Professor(Color.RED));
        } catch (ProfessorFullException e) {
            fail();
        }

        // fill island
        for (int i = 0; i < 3; i++) {
            gameModel.getIslandTileByID(1).addStudent(new Student(3000 + i, Color.RED));
        }

        resolveIsland.solveIsland(gameLobby, 1);
        assertEquals(player.get(0).getColor(), gameModel.getIslandGroupByID(1).getTowersColor());

        print();
    }

    @Test
    public void FullTowersException() {

        // set resolve method
        resolveIsland = new MushroomFanaticResolveIsland();
        try {
            for (int i = 0; i < 7; i++) {
                player.get(0).getSchoolBoard().removeTower();
            }
            fail();
        } catch (Exception e) {
            // ok
        }

        // set mushroom fanatic color
        resolveIsland = new MushroomFanaticResolveIsland();
        assertNotNull(gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC));
        gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).setColor(Color.RED);

        // assign profesors
        try {
            player.get(0).getSchoolBoard().addProfessor(new Professor(Color.GREEN));
            player.get(0).getSchoolBoard().addProfessor(new Professor(Color.RED));
        } catch (ProfessorFullException e) {
            fail();
        }

        // fill island
        for (int i = 0; i < 3; i++) {
            gameModel.getIslandTileByID(1).addStudent(new Student(3000 + i, Color.RED));
        }
        gameModel.getIslandTileByID(1).addStudent(new Student(4000, Color.GREEN));

        assertThrows(RuntimeException.class, () -> resolveIsland.solveIsland(gameLobby, 1));
        assertNull(gameModel.getIslandGroupByID(1).getTowersColor());

        print();
    }
}
