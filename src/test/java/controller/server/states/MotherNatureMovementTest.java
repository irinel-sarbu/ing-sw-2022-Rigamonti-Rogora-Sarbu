package controller.server.states;

import controller.server.GameLobby;
import exceptions.AssistantNotInDeckException;
import exceptions.EmptyNoEntryListException;
import exceptions.IllegalMovementException;
import exceptions.ProfessorFullException;
import model.GameModel;
import model.Player;
import model.board.Professor;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MotherNatureMovementTest {

    private static MotherNatureMovement motherNatureMovement;
    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> player;

    @BeforeEach
    public void StudentMovement() {
        Random.setSeed(2);

        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server(5000));

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.RESOLVE_ISLAND);
        player = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(player.get(0));
        motherNatureMovement = new DefaultMotherNatureMovement();

        int assistants = player.get(0).getAssistants().size();
        try {
            player.get(0).pushFoldDeck(player.get(0).removeCard(0));
        } catch (AssistantNotInDeckException e) {
            fail();
        }
        assertEquals(assistants - 1, player.get(0).getAssistants().size());
        assertNotNull(player.get(0).peekFoldDeck());
    }

    @Test
    public void defaultMotherNatureMovement() {

        // illegalMovement
        assertThrows(IllegalMovementException.class, () -> motherNatureMovement.moveMotherNature(gameLobby, 2));

        try {
            // legal movement
            motherNatureMovement.moveMotherNature(gameLobby, 1);
            assertEquals(GameState.TURN_EPILOGUE, gameLobby.getCurrentGameState());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void postmanMotherNatureMovement() {
        motherNatureMovement = new PostmanMotherNatureMovement();

        // illegalMovement
        assertThrows(IllegalMovementException.class, () -> motherNatureMovement.moveMotherNature(gameLobby, 2 + 2));

        try {
            // legal movement
            motherNatureMovement.moveMotherNature(gameLobby, 1 + 2);
            assertEquals(GameState.TURN_EPILOGUE, gameLobby.getCurrentGameState());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void nextState1() {
        try {
            gameModel.getIslandGroupByID(5).addNoEntry(gameModel.getCharacterByType(CharacterType.GRANNY_HERBS).removeNoEntryTile());
            assertEquals(1, gameModel.getIslandGroupByID(5).getNoEntrySize());
            motherNatureMovement.moveMotherNature(gameLobby, 1);
            assertEquals(0, gameModel.getIslandGroupByID(5).getNoEntrySize());
        } catch (EmptyNoEntryListException e) {
            fail();
        }
    }

    @Test
    public void nextState2() {
        try {
            for (int i = 0; i < 5; i++) {
                player.get(0).getSchoolBoard().removeTower();
            }
        } catch (Exception e) {
            fail();
        }

        try {
            player.get(0).getSchoolBoard().addProfessor(new Professor(Color.GREEN));
        } catch (ProfessorFullException e) {
            fail();
        }

        motherNatureMovement.moveMotherNature(gameLobby, 1);
        assertEquals(GameState.GAME_OVER, gameLobby.getCurrentGameState());

    }

    @Test
    public void nextState3() {

        try {
            player.get(0).getSchoolBoard().addProfessor(new Professor(Color.GREEN));
        } catch (ProfessorFullException e) {
            fail();
        }

        for (int i = 0; i < 4; i++) {
            gameModel.getIslandGroups().get(0).setTowersColor(TowerColor.GRAY);
            gameModel.getIslandGroups().get(1).setTowersColor(TowerColor.GRAY);
            gameModel.joinAdjacent(0);
        }
        for (int i = 2; i < 7; i++) {
            gameModel.getIslandGroups().get(2).setTowersColor(TowerColor.GRAY);
            gameModel.getIslandGroups().get(3).setTowersColor(TowerColor.GRAY);
            gameModel.joinAdjacent(2);
        }

        motherNatureMovement.moveMotherNature(gameLobby, 1);
        assertEquals(GameState.GAME_OVER, gameLobby.getCurrentGameState());
    }

    @Test
    public void nextStateNormal() {
        Random.setSeed(2);

        gameLobby = new GameLobby(3, GameMode.NORMAL, "00000", new Server(5000));

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.RESOLVE_ISLAND);
        player = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(player.get(0));
        motherNatureMovement = new DefaultMotherNatureMovement();

        int assistants = player.get(0).getAssistants().size();
        try {
            player.get(0).pushFoldDeck(player.get(0).removeCard(0));
        } catch (AssistantNotInDeckException e) {
            fail();
        }
        assertEquals(assistants - 1, player.get(0).getAssistants().size());
        assertNotNull(player.get(0).peekFoldDeck());

        // illegalMovement
        assertThrows(IllegalMovementException.class, () -> motherNatureMovement.moveMotherNature(gameLobby, 2));

        try {
            // legal movement
            motherNatureMovement.moveMotherNature(gameLobby, 1);
            assertEquals(GameState.TURN_EPILOGUE, gameLobby.getCurrentGameState());
        } catch (Exception e) {
            fail();
        }
    }
}
