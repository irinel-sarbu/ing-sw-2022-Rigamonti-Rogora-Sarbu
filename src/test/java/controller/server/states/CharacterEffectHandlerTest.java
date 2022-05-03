package controller.server.states;

import controller.server.GameLobby;
import model.GameModel;
import model.Player;
import network.server.ClientHandler;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.GameMode;
import util.GameState;
import util.TowerColor;
import util.Wizard;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CharacterEffectHandlerTest {

    private static CharacterEffectHandler characterEffectHandler;
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
        try {
            clientSocketConnection = new ClientHandler(server, socket);
        } catch (IOException e) {
            System.out.println("cacca");
        }

        gameLobby = new GameLobby(3, GameMode.NORMAL, "00000", new Server(5000));

        gameModel = gameLobby.getModel();

        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.NORMAL));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.PLANNING);
        players = new ArrayList<>(gameLobby.getOrder());

        characterEffectHandler = new CharacterEffectHandler();
    }

    @Test
    public void monkEffect() {
        // TODO: when game is created it automatically generate 3 random characters
    }
}
