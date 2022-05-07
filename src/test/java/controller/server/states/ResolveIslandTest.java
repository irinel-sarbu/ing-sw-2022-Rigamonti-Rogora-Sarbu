package controller.server.states;

import controller.server.GameLobby;
import exceptions.EntranceFullException;
import exceptions.StudentNotFoundException;
import model.GameModel;
import model.Player;
import model.board.CloudTile;
import model.board.IslandGroup;
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

public class ResolveIslandTest {
    private static ResolveIsland resolveIsland;
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
        uuid = UUID.randomUUID();
        Random.setSeed(0);
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
        gameLobby.setGameState(GameState.RESOLVE_ISLAND);
        player = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(player.get(0));

        resolveIsland = new DefaultResolveIsland();

        // empty player 0 entrance
        try {
            while (true) {
                player.get(0).getSchoolBoard().removeFromEntrance(firstOf(0));
            }
        } catch (IndexOutOfBoundsException e) {
            try {
                // fill player 0 entrance with GREEN students only
                int id = 1000;
                while (true) {
                    player.get(0).getSchoolBoard().addToEntrance(new Student(id, Color.GREEN));
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
    public void solveIsland() {
        studentMovement = new DefaultStudentMovement();
        gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
        int ID = player.get(0).getSchoolBoard().getEntranceStudents().get(0).getID();

        try {
            assertNull(gameModel.getIslandGroupByID(1).getTowersColor());
            studentMovement.moveStudentToDining(gameLobby, player.get(0), firstOf(0));
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), 1);
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            resolveIsland.solveIsland(gameLobby, 1);
            assertEquals(gameModel.getPlayerByID(0).getColor(), gameModel.getIslandTileByID(1).getTowerColor());

            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), 2);
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            resolveIsland.solveIsland(gameLobby, 2);
            assertEquals(11, gameModel.getIslandGroups().size());

            gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), 3);
            resolveIsland.solveIsland(gameLobby, 2);
            assertEquals(10, gameModel.getIslandGroups().size());

            System.out.println(IslandGroup.allToString(gameModel.getIslandGroups()));


        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }
    }

    // TODO: non default islands
}
