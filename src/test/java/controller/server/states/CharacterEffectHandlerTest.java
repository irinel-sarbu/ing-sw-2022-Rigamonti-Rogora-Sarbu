package controller.server.states;

import controller.server.GameLobby;
import exceptions.StudentNotFoundException;
import model.GameModel;
import model.Player;
import model.board.IslandTile;
import model.expert.CharacterCard;
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

//IMPORTANT: COIN checks happen in the gameLobby when the event is caught, therefore are not tested HERE

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

    private void setUP() {
        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server(5000));
        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        players = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(players.get(0));
        characterEffectHandler = new CharacterEffectHandler();
    }

    @Test
    public void monkEffect() {
        Random.setSeed(1750);
        setUP();
        CharacterCard monk = gameModel.getCharacterByType(CharacterType.MONK);
        int studentID = monk.getStudents().get(0).getID();
        Color studentColor = monk.getStudents().get(0).getColor();
        int initialBagSize = gameModel.getBag().getRemainingStudents();
        IslandTile islandTile = gameModel.getIslandTileByID(0);
        int initialTileSize = islandTile.getStudentsNumber(studentColor);
        int initialMonkCost = monk.getCost();

        characterEffectHandler.monkEffect(gameLobby, studentID, 0);

        assertEquals(initialBagSize, gameModel.getBag().getRemainingStudents() + 1);
        assertEquals(initialTileSize, islandTile.getStudentsNumber(studentColor) - 1);
        assertEquals(initialMonkCost, monk.getCost() - 1);

        characterEffectHandler.monkEffect(gameLobby, 190, 0);

        Random.setSeed(1750);
        setUP();
        monk = gameModel.getCharacterByType(CharacterType.MONK);
        studentID = monk.getStudents().get(0).getID();
        studentColor = monk.getStudents().get(0).getColor();
        initialBagSize = gameModel.getBag().getRemainingStudents();
        islandTile = gameModel.getIslandTileByID(0);
        initialTileSize = islandTile.getStudentsNumber(studentColor);
        initialMonkCost = monk.getCost();

        do {
            gameModel.getBag().pull();
        } while (!gameModel.getBag().isEmpty());
        characterEffectHandler.monkEffect(gameLobby, 190, 0);

        assertEquals(0, gameModel.getBag().getRemainingStudents());
        assertEquals(initialTileSize, islandTile.getStudentsNumber(studentColor));
        assertEquals(initialMonkCost, monk.getCost());

        characterEffectHandler.monkEffect(gameLobby, studentID, 0);

        assertEquals(0, gameModel.getBag().getRemainingStudents());
        assertEquals(initialTileSize, islandTile.getStudentsNumber(studentColor) - 1);
        assertEquals(initialMonkCost, monk.getCost() - 1);
    }

    @Test
    public void farmerEffect() {
        Random.setSeed(14);
        setUP();
        CharacterCard farmer = gameModel.getCharacterByType(CharacterType.FARMER);
        int initialFarmerCost = farmer.getCost();

        characterEffectHandler.farmerEffect(gameLobby);

        assertEquals(initialFarmerCost, farmer.getCost() - 1);
    }
}
