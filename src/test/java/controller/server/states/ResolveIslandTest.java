package controller.server.states;

import controller.server.GameLobby;
import exceptions.EntranceFullException;
import exceptions.StudentNotFoundException;
import exceptions.WrongPhaseException;
import exceptions.WrongPlayerException;
import model.GameModel;
import model.Player;
import model.board.IslandGroup;
import model.board.IslandTile;
import model.board.Student;
import network.server.ClientSocketConnection;
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
    private static ClientSocketConnection clientSocketConnection;

    private static IslandGroup someIslandGroup;

    public int testEmpty() throws IndexOutOfBoundsException {
        for (IslandGroup ig : gameModel.getIslandGroups()) {
            int cnt = 0;
            for (Color c : Color.values()) {
                cnt += ig.getStudentsNumber(c);
            }
            if (cnt == 0) {
                return ig.getIslandGroupID();
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public int firstOf(int i) {
        return player.get(i).getSchoolBoard().getEntranceStudents().get(0).getID();
    }

    @BeforeEach
    public void StudentMovement() {
        server = new Server();
        socket = new Socket();
        uuid = UUID.randomUUID();
        try {
            clientSocketConnection = new ClientSocketConnection(server, socket, uuid);
        } catch (IOException e) {
            System.out.println("cacca");
        }

        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server());

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.RESOLVE_ISLAND);
        player = new ArrayList<>(gameLobby.getOrder());

        resolveIsland = new DefaultResolveIsland();
        someIslandGroup = gameModel.getIslandGroupByID(testEmpty());

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
    public void solveIsland() {
        studentMovement = new DefaultStudentMovement();
        gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
        int ID = player.get(0).getSchoolBoard().getEntranceStudents().get(0).getID();

        try {
            assertNull(someIslandGroup.getTowersColor());
            studentMovement.moveStudentToDining(gameLobby, player.get(0), firstOf(0));
            studentMovement.moveStudentToIsland(gameLobby, player.get(0), firstOf(0), someIslandGroup.getIslandGroupID());
            gameLobby.setGameState(GameState.RESOLVE_ISLAND);
            resolveIsland.solveIsland(gameLobby, someIslandGroup.getIslandGroupID());

            assertEquals(player.get(0).getSchoolBoard().getTowers().get(0).getColor(), someIslandGroup.getTowersColor());

        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }
    }

    // TODO: non default islands
}
