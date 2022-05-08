package controller.server.states;

import controller.server.GameLobby;
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

    @Test
    public void heraldEffect() {
        Random.setSeed(14);
        setUP();
        CharacterCard herald = gameModel.getCharacterByType(CharacterType.HERALD);
        int initialCost = herald.getCost();

        gameModel.getIslandGroupByID(0).getIslands().get(0).addStudent(new Student(123, Color.BLUE));
        try {
            gameLobby.getCurrentPlayer().getSchoolBoard().addProfessor(new Professor(Color.BLUE));
        } catch (ProfessorFullException e) {
            fail();
        }
        characterEffectHandler.heraldEffect(gameLobby, 0);

        assertEquals(initialCost, herald.getCost() - 1);
        assertEquals(gameModel.getIslandGroupByID(0).getTowersColor(), gameLobby.getCurrentPlayer().getColor());
        int i = 0;
        do {
            gameModel.getIslandGroups().remove(i);
        } while (gameModel.getIslandGroups().size() > 3);
        characterEffectHandler.heraldEffect(gameLobby, 0);

        try {
            while (true) {
                gameLobby.getCurrentPlayer().getSchoolBoard().removeTower();
            }
        } catch (TowersIsEmptyException e) {
            //doNothing
        } finally {
            characterEffectHandler.heraldEffect(gameLobby, 0);
        }
    }

    @Test
    public void postmanEffect() {
        Random.setSeed(1750);
        setUP();
        CharacterCard postman = gameModel.getCharacterByType(CharacterType.POSTMAN);
        int initialCost = postman.getCost();

        characterEffectHandler.postmanEffect(gameLobby);

        assertEquals(initialCost, postman.getCost() - 1);
    }

    @Test
    public void grannyHerbsEffect() {
        Random.setSeed(3);
        setUP();
        CharacterCard granny = gameModel.getCharacterByType(CharacterType.GRANNY_HERBS);

        int initialCost = granny.getCost();
        int initialNoEntryTilesOnGranny = granny.getNoEntryTiles().size();
        int initialNoEntryTilesOnIsland = gameModel.getIslandGroups().get(0).getNoEntrySize();
        characterEffectHandler.grannyHerbsEffect(gameLobby, 0);
        assertEquals(initialCost, granny.getCost() - 1);
        assertEquals(initialNoEntryTilesOnGranny, granny.getNoEntryTiles().size() + 1);
        assertEquals(initialNoEntryTilesOnIsland, gameModel.getIslandGroups().get(0).getNoEntrySize() - 1);

        initialCost = granny.getCost();
        initialNoEntryTilesOnGranny = granny.getNoEntryTiles().size();
        initialNoEntryTilesOnIsland = gameModel.getIslandGroups().get(0).getNoEntrySize();
        characterEffectHandler.grannyHerbsEffect(gameLobby, 0);
        assertEquals(initialCost, granny.getCost() - 1);
        assertEquals(initialNoEntryTilesOnGranny, granny.getNoEntryTiles().size() + 1);
        assertEquals(initialNoEntryTilesOnIsland, gameModel.getIslandGroups().get(0).getNoEntrySize() - 1);

        initialCost = granny.getCost();
        initialNoEntryTilesOnGranny = granny.getNoEntryTiles().size();
        initialNoEntryTilesOnIsland = gameModel.getIslandGroups().get(0).getNoEntrySize();
        characterEffectHandler.grannyHerbsEffect(gameLobby, 0);
        assertEquals(initialCost, granny.getCost() - 1);
        assertEquals(initialNoEntryTilesOnGranny, granny.getNoEntryTiles().size() + 1);
        assertEquals(initialNoEntryTilesOnIsland, gameModel.getIslandGroups().get(0).getNoEntrySize() - 1);

        initialCost = granny.getCost();
        initialNoEntryTilesOnGranny = granny.getNoEntryTiles().size();
        initialNoEntryTilesOnIsland = gameModel.getIslandGroups().get(0).getNoEntrySize();
        characterEffectHandler.grannyHerbsEffect(gameLobby, 0);
        assertEquals(initialCost, granny.getCost() - 1);
        assertEquals(initialNoEntryTilesOnGranny, granny.getNoEntryTiles().size() + 1);
        assertEquals(initialNoEntryTilesOnIsland, gameModel.getIslandGroups().get(0).getNoEntrySize() - 1);

        initialCost = granny.getCost();
        initialNoEntryTilesOnGranny = granny.getNoEntryTiles().size();
        initialNoEntryTilesOnIsland = gameModel.getIslandGroups().get(0).getNoEntrySize();
        characterEffectHandler.grannyHerbsEffect(gameLobby, 0);
        assertEquals(initialCost, granny.getCost() - 1);
        assertEquals(initialNoEntryTilesOnGranny, granny.getNoEntryTiles().size());
        assertEquals(initialNoEntryTilesOnIsland, gameModel.getIslandGroups().get(0).getNoEntrySize());
    }

    @Test
    public void centaurEffect() {
        Random.setSeed(0);
        setUP();

        CharacterCard centaur = gameModel.getCharacterByType(CharacterType.CENTAUR);
        int initialCost = centaur.getCost();

        characterEffectHandler.centaurEffect(gameLobby);

        assertEquals(initialCost, centaur.getCost() - 1);
    }

    @Test
    public void jesterEffect() {
        Random.setSeed(0);
        setUP();

        CharacterCard jester = gameModel.getCharacterByType(CharacterType.JESTER);
        int initialCost = jester.getCost();
        int entranceSize = gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().size();
        int jesterSize = jester.getStudents().size();

        List<Student> jesterStudents = new ArrayList<>(), entranceStudents = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            jesterStudents.add(jester.getStudents().get(i));
            entranceStudents.add(gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().get(i));
        }

        List<Integer> jesterIDs = new ArrayList<>(), entranceIDs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            jesterIDs.add(jesterStudents.get(i).getID());
            entranceIDs.add(entranceStudents.get(i).getID());
        }

        //System.out.println("------>Jester students before" + jester.getStudents());
        //System.out.println("------>Entrance students before" + gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents());

        characterEffectHandler.jesterEffect(gameLobby, entranceIDs, jesterIDs);

        //System.out.println("------>Jester students after" + jester.getStudents());
        //System.out.println("------>Entrance students after" + gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents());

        assertEquals(initialCost, jester.getCost() - 1);
        assertEquals(entranceSize, gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().size());
        assertEquals(jesterSize, jester.getStudents().size());

        for (int i = 0; i < 3; i++) {
            assertTrue(gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().contains(jesterStudents.get(i)));
            assertTrue(jester.getStudents().contains(entranceStudents.get(i)));
        }

        List<Integer> longerEntrance = new ArrayList<>(entranceIDs);
        longerEntrance.add(445);
        assertThrows(LengthMismatchException.class, () -> characterEffectHandler.jesterEffect(gameLobby, longerEntrance, jesterIDs));

        longerEntrance.remove(0);
        //should send an error message
        characterEffectHandler.jesterEffect(gameLobby, longerEntrance, jesterIDs);

        int j = 0;
        try {
            while (true) {
                gameLobby.getCurrentPlayer().getSchoolBoard().addToEntrance(new Student(j, Color.BLUE));
                j++;
            }
        } catch (EntranceFullException e) {
            gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().add(new Student(j + 1, Color.BLUE));
        }
        //should send an error message
        characterEffectHandler.jesterEffect(gameLobby, longerEntrance, jesterIDs);
    }

    @Test
    public void minstrelEffect() {
        Random.setSeed(3);
        setUP();
        //In this seed the first 2 students are pink
        CharacterCard minstrel = gameModel.getCharacterByType(CharacterType.MINSTREL);
        int initialCost = minstrel.getCost();
        int entranceSize = gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().size();
        try {
            gameLobby.getCurrentPlayer().getSchoolBoard().addToDiningRoom(new Student(1, Color.BLUE));
            gameLobby.getCurrentPlayer().getSchoolBoard().addToDiningRoom(new Student(2, Color.PINK));
        } catch (DiningRoomFullException e) {
            fail();
        }
        int numOfBlues = gameLobby.getCurrentPlayer().getSchoolBoard().getStudentsOfColor(Color.BLUE);
        int numOfPinks = gameLobby.getCurrentPlayer().getSchoolBoard().getStudentsOfColor(Color.PINK);

        List<Student> entranceStudents = new ArrayList<>();
        entranceStudents.add(gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().get(0));
        entranceStudents.add(gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().get(1));

        List<Integer> entranceIDs = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.PINK);
        entranceIDs.add(entranceStudents.get(0).getID());
        entranceIDs.add(entranceStudents.get(1).getID());

        characterEffectHandler.minstrelEffect(gameLobby, entranceIDs, colors);
        assertEquals(initialCost, minstrel.getCost() - 1);
        assertEquals(entranceSize, gameLobby.getCurrentPlayer().getSchoolBoard().getEntranceStudents().size());
        assertEquals(numOfBlues, gameLobby.getCurrentPlayer().getSchoolBoard().getStudentsOfColor(Color.BLUE) + 1);
        assertEquals(numOfPinks, gameLobby.getCurrentPlayer().getSchoolBoard().getStudentsOfColor(Color.PINK) - 1);
        //should send an error message
        characterEffectHandler.minstrelEffect(gameLobby, entranceIDs, colors);
    }

    @Test
    public void knightEffect() {
        Random.setSeed(3);
        setUP();

        CharacterCard knight = gameModel.getCharacterByType(CharacterType.KNIGHT);
        int initialCost = knight.getCost();

        characterEffectHandler.knightEffect(gameLobby);

        assertEquals(initialCost, knight.getCost() - 1);
    }

    @Test
    public void princessEffect() {
        Random.setSeed(1750);
        setUP();

        CharacterCard princess = gameModel.getCharacterByType(CharacterType.PRINCESS);
        int initialCost = princess.getCost();
        int initialSize = princess.getStudents().size();
        Color selectedColor = princess.getStudents().get(0).getColor();
        int initialDiningSize = gameLobby.getCurrentPlayer().getSchoolBoard().getStudentsOfColor(selectedColor);

        characterEffectHandler.princessEffect(gameLobby, princess.getStudents().get(0).getID());

        assertEquals(initialCost, princess.getCost() - 1);
        assertEquals(initialSize, princess.getStudents().size());
        assertEquals(initialDiningSize, gameLobby.getCurrentPlayer().getSchoolBoard().getStudentsOfColor(selectedColor) - 1);

        do {
            gameModel.getBag().pull();
        } while (!gameModel.getBag().isEmpty());
        characterEffectHandler.princessEffect(gameLobby, princess.getStudents().get(0).getID());
        assertEquals(initialSize, princess.getStudents().size() + 1);

        //should send an error
        characterEffectHandler.princessEffect(gameLobby, 1234);
    }

    @Test
    public void mushroomFanaticEffect() {
        Random.setSeed(0);
        setUP();

        CharacterCard fanatic = gameModel.getCharacterByType(CharacterType.MUSHROOM_FANATIC);
        int initialCost = fanatic.getCost();

        characterEffectHandler.mushroomFanaticEffect(gameLobby, Color.BLUE);

        assertEquals(initialCost, fanatic.getCost() - 1);
    }

    @Test
    public void thiefEffect() {
        Random.setSeed(14);
        setUP();

        CharacterCard thief = gameModel.getCharacterByType(CharacterType.THIEF);
        int initialCost = thief.getCost();
        try {
            gameModel.getPlayerByID(0).getSchoolBoard().addToDiningRoom(new Student(1, Color.BLUE));
            gameModel.getPlayerByID(1).getSchoolBoard().addToDiningRoom(new Student(2, Color.BLUE));
            gameModel.getPlayerByID(1).getSchoolBoard().addToDiningRoom(new Student(4, Color.BLUE));
            gameModel.getPlayerByID(1).getSchoolBoard().addToDiningRoom(new Student(8, Color.BLUE));
            gameModel.getPlayerByID(2).getSchoolBoard().addToDiningRoom(new Student(16, Color.BLUE));
            gameModel.getPlayerByID(2).getSchoolBoard().addToDiningRoom(new Student(32, Color.BLUE));
            gameModel.getPlayerByID(2).getSchoolBoard().addToDiningRoom(new Student(64, Color.BLUE));
            gameModel.getPlayerByID(2).getSchoolBoard().addToDiningRoom(new Student(128, Color.BLUE));
        } catch (DiningRoomFullException | PlayerNotFoundException e) {
            fail();
        }

        characterEffectHandler.thiefEffect(gameLobby, Color.BLUE);

        try {
            assertEquals(0, gameModel.getPlayerByID(0).getSchoolBoard().getStudentsOfColor(Color.BLUE));
            assertEquals(0, gameModel.getPlayerByID(1).getSchoolBoard().getStudentsOfColor(Color.BLUE));
            assertEquals(1, gameModel.getPlayerByID(2).getSchoolBoard().getStudentsOfColor(Color.BLUE));
        } catch (PlayerNotFoundException e) {
            fail();
        }
        assertEquals(initialCost, thief.getCost() - 1);
    }
}
