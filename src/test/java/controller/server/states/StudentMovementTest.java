package controller.server.states;

import controller.server.GameLobby;
import exceptions.*;
import model.GameModel;
import model.Player;
import model.board.Student;
import network.server.ClientHandler;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentMovementTest {
    private static StudentMovement studentMovement;
    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> player;
    private static Server server;
    private static Socket socket;
    private static UUID uuid;
    private static ClientHandler clientSocketConnection;

    public int firstOf(int i) {
        return player.get(i).getSchoolBoard().getEntranceStudents().get(0).getID();
    }

    @BeforeEach
    public void StudentMovement() {
        server = new Server(5000);
        socket = new Socket();
        try {
            clientSocketConnection = new ClientHandler(server, socket);
        } catch (IOException e) {
            System.out.println("cacca");
        }

        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server(5000));

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
        player = new ArrayList<>(gameLobby.getOrder());
        studentMovement = new DefaultStudentMovement();

        // empty player 0 entrance
        try {
            while (true) {
                player.get(0).getSchoolBoard().removeFromEntrance(firstOf(0));
            }
        } catch (IndexOutOfBoundsException e) {
            try {
                // fill player 0 entrance with blue students only
                int id = 1000;
                while (true) {
                    player.get(0).getSchoolBoard().addToEntrance(new Student(id, Color.BLUE));
                    id++;
                }
            } catch (EntranceFullException ex) {
                // do nothing
            }
        } catch (StudentNotFoundException e) {
            System.err.println("some error");
            fail();
        }
    }

    @Test
    public void moveStudentToDiningFirst() {

        // set player 1 has professor
        try {
            player.get(1).getSchoolBoard().addToDiningRoom(new Student(2000, Color.BLUE));
            player.get(1).getSchoolBoard().addProfessor(gameModel.removeProfessor(Color.BLUE));
        } catch (Exception e) {
            System.out.println(e + "not expected");
            fail();
        }

        // move just enough students to obtain a coin and trigger or not steal events
        for (int i = 0; i < gameLobby.getMaxStudentsMoved() + 1; i++) {
            int coins = player.get(0).getSchoolBoard().getCoinSupply().getNumOfCoins();
            int totalCoins = gameModel.getCoinSupply().getNumOfCoins();
            try {
                if (i == gameLobby.getMaxStudentsMoved()) {
                    assertThrows(WrongPhaseException.class, () -> studentMovement.moveStudentToDining(gameLobby, player.get(0), firstOf(0)));
                } else {
                    studentMovement.moveStudentToDining(gameLobby, player.get(0), firstOf(0));
                }
            } catch (Exception e) {
                System.err.println(player.get(0).getSchoolBoard());
                System.err.println(e + " not expected");
                fail();
            }
            assertEquals(coins + ((i == 2) ? 1 : 0), player.get(0).getSchoolBoard().getCoinSupply().getNumOfCoins());
            assertEquals(totalCoins, gameModel.getCoinSupply().getNumOfCoins() + ((i == 2) ? 1 : 0));

            if (studentMovement instanceof DefaultStudentMovement) {
                if (i == 1) {
                    assertTrue(player.get(0).getSchoolBoard().hasProfessor(Color.BLUE));
                    try {
                        player.get(1).getSchoolBoard().addToDiningRoom(new Student(2001, Color.BLUE));
                        player.get(1).getSchoolBoard().addToDiningRoom(new Student(2002, Color.BLUE));
                        player.get(1).getSchoolBoard().addToDiningRoom(new Student(2003, Color.BLUE));
                        player.get(1).getSchoolBoard().addProfessor(player.get(0).getSchoolBoard().removeProfessorByColor(Color.BLUE));
                    } catch (Exception ne) {
                        System.err.println(ne + " not expected");
                        fail();
                    }
                    studentMovement = new FarmerStudentMovement();
                } else {
                    assertFalse(player.get(0).getSchoolBoard().hasProfessor(Color.BLUE));
                }
            } else {
                if (i == 2) {
                    assertFalse(player.get(0).getSchoolBoard().hasProfessor(Color.BLUE));
                } else {
                    assertTrue(player.get(0).getSchoolBoard().hasProfessor(Color.BLUE));
                }
            }
        }

        assertEquals(GameState.MOTHERNATURE_MOVEMENT, gameLobby.getCurrentGameState());
        assertEquals(player.get(0), gameLobby.getCurrentPlayer());
        assertThrows(WrongPhaseException.class, () -> studentMovement.moveStudentToDining(gameLobby, player.get(0), 0));
        gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
        gameLobby.setCurrentPlayer(player.get(1));
        assertThrows(WrongPlayerException.class, () -> studentMovement.moveStudentToDining(gameLobby, player.get(0), 0));
    }

    @Test
    public void moveStudentToDiningSecond() {
        // ensure professor is not in player school board but is unassigned
        assertFalse(player.get(0).getSchoolBoard().hasProfessor(Color.BLUE));
        assertTrue(gameModel.getUnassignedProfessors().contains(Color.BLUE));

        try {
            gameModel.getCoinSupply().removeCoins(gameModel.getCoinSupply().getNumOfCoins());
        } catch (supplyEmptyException e) {
            System.err.println(e + " not expected");
            fail();
        }

        for (int i = 0; i < 3; i++) {
            try {
                studentMovement.moveStudentToDining(gameLobby, player.get(0), firstOf(0));
            } catch (Exception e) {
                fail();
            }
            // both ensure to move professor from game model to school board and prevent self steal
            assertTrue(player.get(0).getSchoolBoard().hasProfessor(Color.BLUE));
            assertFalse(gameModel.getUnassignedProfessors().contains(Color.BLUE));
            assertEquals(0, player.get(0).getSchoolBoard().getCoinSupply().getNumOfCoins());
        }

    }

    @Test
    public void moveStudentToIsland() {

        int ID = player.get(0).getSchoolBoard().getEntranceStudents().get(0).getID();
        int count = gameModel.getIslandTileByID(0).getStudentsNumber(Color.BLUE);
        try {
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), ID, 0);
        } catch (Exception e) {
            System.err.println(e + " not expected");
        }
        assertEquals(count + 1, gameModel.getIslandTileByID(0).getStudentsNumber(Color.BLUE));

        gameLobby.setCurrentPlayer(player.get(1));
        assertThrows(WrongPlayerException.class, () -> studentMovement.moveStudentToIsland(gameLobby, player.get(0), 0, 0));
        gameLobby.setGameState(GameState.MOTHERNATURE_MOVEMENT);
        assertThrows(WrongPhaseException.class, () -> studentMovement.moveStudentToIsland(gameLobby, player.get(0), 0, 0));
    }

}
