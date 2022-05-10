package controller.server;

import controller.server.GameLobby;
import controller.server.states.CharacterEffectHandler;
import exceptions.*;
import model.GameModel;
import model.Player;
import model.board.Bag;
import model.board.IslandTile;
import model.board.Professor;
import model.board.Student;
import model.expert.CharacterCard;
import network.server.ClientHandler;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.*;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

public class GameLobbyTest {
    private static Server server;
    private static Socket socket;
    private static UUID uuid;
    private static ClientHandler clientSocketConnection;
    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> players;

    @BeforeEach
    public void CharacterEffectHandler() {
        server = new Server(5000);
        socket = new Socket();
        uuid = UUID.randomUUID();

        try {
            clientSocketConnection = new ClientHandler(server, socket);
        } catch (IOException e) {
            System.out.println("Test");
        }
    }

    //RANDOM SEEDS:
    // -- 0 = JESTER, MUSHROOM FANATIC, CENTAUR
    // -- 3 = GRANNY HERBS, MINSTREL, KNIGHT
    // -- 14 = HERALD, FARMER, THIEF
    // -- 1750 = PRINCESS, POSTMAN, MONK

    private void setUPExpert() {
        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server(5000));
        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        players = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(players.get(0));
    }

    private void setUPNormal() {
        gameLobby = new GameLobby(2, GameMode.NORMAL, "00000", new Server(5000));
        gameModel = gameLobby.getModel();
        for (int i = 0; i < 2; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.NORMAL));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        players = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(players.get(0));
    }

    //Testing Methods of the Server controller that have not already been tested (for necessity) in the sub-classes server.states

    @Test
    public void getLobby() {
        Random.setSeed(0);
        setUPExpert();

        assertEquals(5, gameLobby.getLobbyCode().length());
        assertSame(gameLobby.getLobbyState(), LobbyState.INIT);

        Random.setSeed(3);
        setUPNormal();

        assertEquals(5, gameLobby.getLobbyCode().length());
        assertSame(gameLobby.getLobbyState(), LobbyState.INIT);

        try {
            ClientHandler clientTest = new ClientHandler(server, socket);

            assertThrows(NullPointerException.class, () -> gameLobby.addClientToLobby("client1", clientTest));

            //Should print null because can't test network part
            System.out.println(gameLobby.getClientByName("client1"));

            assertEquals("client1", gameLobby.getPlayerNameBySocket(clientTest));
        } catch (IOException e) {
            fail();
        }
    }
}
