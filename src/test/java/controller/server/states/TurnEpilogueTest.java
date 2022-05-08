package controller.server.states;

import controller.server.GameLobby;
import exceptions.EntranceFullException;
import exceptions.StudentNotFoundException;
import model.GameModel;
import model.Player;
import model.board.Student;
import network.server.ClientHandler;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import util.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

public class TurnEpilogueTest {

    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> player;
    private static Server server;
    private static Socket socket;
    private static UUID uuid;
    private static ClientHandler clientSocketConnection;

    private static TurnEpilogue turnEpilogue;

    @BeforeEach
    public void StudentMovement() {
        server = new Server(5000);
        socket = new Socket();
        uuid = UUID.randomUUID();
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
        turnEpilogue = new TurnEpilogue();

    }
}
