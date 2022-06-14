package controller.server;

import controller.server.GameLobby;
import controller.server.states.*;
import eventSystem.events.Event;
import eventSystem.events.network.client.*;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
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

    private static List<Student> entranceStudents, jesterStudents, someStudents;


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

        assertEquals(cardCost + 1, gameModel.getCharacterByType(type).getCost());
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
    public void GrannyEffect() {

        // wrong player
        Random.setSeed(3);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.GRANNY_HERBS).getCost();

        EUseGrannyEffect wrongPlayer = new EUseGrannyEffect(0);
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(0, gameModel.getIslandGroupByID(0).getNoEntrySize());
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.GRANNY_HERBS).getCost());

        // not enough coin
        Random.setSeed(3);
        setUPExpert();
        EUseGrannyEffect notEnoughCoin = new EUseGrannyEffect(0);
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertEquals(0, gameModel.getIslandGroupByID(0).getNoEntrySize());
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.GRANNY_HERBS).getCost());

        // right run
        Random.setSeed(3);
        setUPExpert();
        EUseGrannyEffect rightRun = new EUseGrannyEffect(0);
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(1, gameModel.getIslandGroupByID(0).getNoEntrySize());
        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.GRANNY_HERBS).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void HeraldEffect() {

        // wrong player
        Random.setSeed(14);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.HERALD).getCost();

        EUseHeraldEffect wrongPlayer = new EUseHeraldEffect(0);
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.HERALD).getCost());

        // not enough coin
        Random.setSeed(14);
        setUPExpert();
        EUseHeraldEffect notEnoughCoin = new EUseHeraldEffect(0);
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.HERALD).getCost());

        // right run
        Random.setSeed(14);
        setUPExpert();
        EUseHeraldEffect rightRun = new EUseHeraldEffect(0);
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.HERALD).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void ThiefEffect() {

        // wrong player
        Random.setSeed(14);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.THIEF).getCost();

        EUseThiefEffect wrongPlayer = new EUseThiefEffect(Color.GREEN);
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.THIEF).getCost());

        // not enough coin
        Random.setSeed(14);
        setUPExpert();
        EUseThiefEffect notEnoughCoin = new EUseThiefEffect(Color.GREEN);
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.THIEF).getCost());

        // right run
        Random.setSeed(14);
        setUPExpert();
        EUseThiefEffect rightRun = new EUseThiefEffect(Color.GREEN);
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.THIEF).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void MinistrelEffect() {

        // wrong player
        Random.setSeed(3);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.MINSTREL).getCost();

        EUseMinstrelEffect wrongPlayer = new EUseMinstrelEffect(new ArrayList<>(), new ArrayList<>());
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.MINSTREL).getCost());

        // not enough coin
        Random.setSeed(3);
        setUPExpert();
        EUseMinstrelEffect notEnoughCoin = new EUseMinstrelEffect(new ArrayList<>(), new ArrayList<>());
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.MINSTREL).getCost());

        // right run
        Random.setSeed(3);
        setUPExpert();
        EUseMinstrelEffect rightRun = new EUseMinstrelEffect(new ArrayList<>(), new ArrayList<>());
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.MINSTREL).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void JesterEffect() {

        // wrong player
        Random.setSeed(0);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.JESTER).getCost();

        EUseJesterEffect wrongPlayer = new EUseJesterEffect(new ArrayList<>(), new ArrayList<>());
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.JESTER).getCost());

        // not enough coin
        Random.setSeed(0);
        setUPExpert();
        EUseJesterEffect notEnoughCoin = new EUseJesterEffect(new ArrayList<>(), new ArrayList<>());
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.JESTER).getCost());

        // right run
        Random.setSeed(0);
        setUPExpert();
        EUseJesterEffect rightRun = new EUseJesterEffect(new ArrayList<>(), new ArrayList<>());
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.JESTER).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void MonkEffect() {

        // wrong player
        Random.setSeed(1750);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.MONK).getCost();

        EUseMonkEffect wrongPlayer = new EUseMonkEffect(gameModel.getCharacterByType(CharacterType.MONK).getStudents().get(0).getID(), 0);
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.MONK).getCost());

        // not enough coin
        Random.setSeed(1750);
        setUPExpert();
        EUseMonkEffect notEnoughCoin = new EUseMonkEffect(gameModel.getCharacterByType(CharacterType.MONK).getStudents().get(0).getID(), 0);
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.MONK).getCost());

        // right run
        Random.setSeed(1750);
        setUPExpert();
        EUseMonkEffect rightRun = new EUseMonkEffect(gameModel.getCharacterByType(CharacterType.MONK).getStudents().get(0).getID(), 0);
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.MONK).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void PrincessEffect() {

        // wrong player
        Random.setSeed(1750);
        setUPExpert();

        int cardCost = gameModel.getCharacterByType(CharacterType.PRINCESS).getCost();

        EUsePrincessEffect wrongPlayer = new EUsePrincessEffect(gameModel.getCharacterByType(CharacterType.PRINCESS).getStudents().get(0).getID());
        wrongPlayer.setClientNickname("player2");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(wrongPlayer));
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.PRINCESS).getCost());

        // not enough coin
        Random.setSeed(1750);
        setUPExpert();
        EUsePrincessEffect notEnoughCoin = new EUsePrincessEffect(gameModel.getCharacterByType(CharacterType.PRINCESS).getStudents().get(0).getID());
        notEnoughCoin.setClientNickname("player1");
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(notEnoughCoin));
        assertFalse(gameLobby.getStudentMovement() instanceof FarmerStudentMovement);
        assertEquals(cardCost, gameModel.getCharacterByType(CharacterType.PRINCESS).getCost());

        // right run
        Random.setSeed(1750);
        setUPExpert();
        EUsePrincessEffect rightRun = new EUsePrincessEffect(gameModel.getCharacterByType(CharacterType.PRINCESS).getStudents().get(0).getID());
        rightRun.setClientNickname("player1");

        gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().addCoins(3);
        assertThrows(NullPointerException.class, () -> gameLobby.playerHasActivatedEffect(rightRun));

        assertEquals(cardCost + 1, gameModel.getCharacterByType(CharacterType.PRINCESS).getCost());
        assertEquals(3 - cardCost, gameLobby.getCurrentPlayer().getSchoolBoard().getCoinSupply().getNumOfCoins());
    }

    @Test
    public void NullSocket() {
        assertNull(gameLobby.getPlayerNameBySocket(null));
    }

    @Test
    public void StudentToDining() {
        Random.setSeed(0);
        setUPNormal();
        gameLobby.setGameState(GameState.STUDENT_MOVEMENT);
        try {
            gameLobby.setCurrentPlayer(gameModel.getPlayerByName("player1"));
            EStudentMovementToDining event = new EStudentMovementToDining(gameModel.getPlayerByName("player1").getSchoolBoard().getEntranceStudents().get(0).getID());
            event.setClientNickname("player1");
            assertThrows(NullPointerException.class, () -> gameLobby.playerHasMovedToDining(event));
        } catch (PlayerNotFoundException e) {
            fail();
        }
    }

    @Test
    public void RemoveClient() {
        Random.setSeed(0);
        setUPNormal();
        gameLobby.endGame();
    }
}
