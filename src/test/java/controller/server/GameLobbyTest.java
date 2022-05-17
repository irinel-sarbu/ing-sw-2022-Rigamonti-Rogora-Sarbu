package controller.server;

import controller.server.GameLobby;
import controller.server.states.*;
import eventSystem.events.Event;
import eventSystem.events.network.client.EUseCharacterEffect;
import eventSystem.events.network.client.EUseFanaticEffect;
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
    public void SetGameLobby() {
        Random.setSeed(0);
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

    private void characterActivatedTest(int seed, CharacterType type) {
        Random.setSeed(seed);
        setUPExpert();
        EUseCharacterEffect someEffect = new EUseCharacterEffect(type);
        someEffect.setClientNickname("player1");

        int cardCost = gameModel.getCharacterByType(type).getCost();
        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(someEffect));

        assertEquals(cardCost+1, gameModel.getCharacterByType(type).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void playerHasActivatedEffect() {

        // wrong player
        Random.setSeed(0);
        setUPExpert();
        EUseCharacterEffect wrongPlayer = new EUseCharacterEffect(CharacterType.JESTER);
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));

        // not a passive character
        Random.setSeed(0);
        setUPExpert();
        EUseCharacterEffect wrongCharacter = new EUseCharacterEffect(CharacterType.JESTER);
        wrongCharacter.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongCharacter));

        // not enough coin
        Random.setSeed(14);
        setUPExpert();
        EUseCharacterEffect notEnoughCoin = new EUseCharacterEffect(CharacterType.FARMER);
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);

        characterActivatedTest(14, CharacterType.FARMER);
        assertTrue(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        characterActivatedTest(1750, CharacterType.POSTMAN);
        assertTrue(gameLobby.getMotherNatureMovement() instanceof PostmanMotherNatureMovement);
        characterActivatedTest(0, CharacterType.CENTAUR);
        assertTrue(gameLobby.getResolveIsland() instanceof CentaurResolveIsland);
        characterActivatedTest(3, CharacterType.KNIGHT);
        assertTrue(gameLobby.getResolveIsland() instanceof KnightResolveIsland);
    }

    @Test
    public void FanaticEffect() {

        // wrong player
        Random.setSeed(0);
        setUPExpert();
        EUseFanaticEffect wrongPlayer = new EUseFanaticEffect(Color.GREEN);
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertNull(gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getColor());

        // not enough coin
        Random.setSeed(0);
        setUPExpert();
        EUseFanaticEffect notEnoughCoin = new EUseFanaticEffect(Color.GREEN);
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertNull(gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getColor());

        // right run
        Random.setSeed(0);
        setUPExpert();
        EUseFanaticEffect rightRun = new EUseFanaticEffect(Color.GREEN);
        rightRun.setClientNickname("player1");

        int cardCost = gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getCost();
        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(Color.GREEN, gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getColor());
        assertEquals(cardCost+1, gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());

    }

}
