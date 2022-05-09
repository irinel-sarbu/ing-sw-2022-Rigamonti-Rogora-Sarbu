package controller.server.states;

import controller.server.GameLobby;
import exceptions.EmptyStudentListException;
import exceptions.NoCloudTileException;
import exceptions.WrongPlayerException;
import model.GameModel;
import model.Player;
import model.board.Student;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnEpilogueTest {

    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> player;
    private static TurnEpilogue turnEpilogue;

    @BeforeEach
    public void StudentMovement() {

        gameLobby = new GameLobby(3, GameMode.EXPERT, "00000", new Server(5000));

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.EXPERT));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.TURN_EPILOGUE);
        player = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(player.get(0));
        turnEpilogue = new TurnEpilogue();
    }

    private int firstOf(int i) {
        return player.get(i).getSchoolBoard().getEntranceStudents().get(0).getID();
    }

    private void empty() {
        try {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 3; j++) {
                    player.get(j).getSchoolBoard().removeFromEntrance(firstOf(j));
                }
            }
            fail();
        } catch (Exception e) {
            // ok
        }
        try {
            for (int i = 0; i < 3; i++) {
                List<Student> old = new ArrayList<>(gameModel.getCloudTile(i).getStudents());
                turnEpilogue.refillFromCloudTile(gameLobby, player.get(i), i);
                for (Student s : old) {
                    assertTrue(player.get(i).getSchoolBoard().getEntranceStudents().contains(s));
                }
                for (Student s : player.get(i).getSchoolBoard().getEntranceStudents()) {
                    assertTrue(old.contains(s));
                }
            }
        } catch (Exception ex) {
            System.err.println(ex + " not expected");
            fail();
        }

    }

    @Test
    public void refillFromCloudTile() {
        empty();
    }

    @Test
    public void emptyBag() {
        try {
            for (int i = 0; i < 100; i++) {
                gameModel.getBag().pull();
            }
            fail();
        } catch (EmptyStudentListException e) {
            // ok
        }

        empty();
        assertEquals(GameState.GAME_OVER, gameLobby.getCurrentGameState());
    }

    @Test
    public void emptyAssistants() {
        try {
            for (int i = 0; i < 11; i++) {
                player.get(1).removeCard(0);
            }
        } catch (Exception e) {
            // ok
        }
        empty();
        assertEquals(GameState.GAME_OVER, gameLobby.getCurrentGameState());
    }

    @Test
    public void refillFromCloudTileException() {
        gameLobby.setCurrentPlayer(player.get(1));
        assertThrows(WrongPlayerException.class, () -> turnEpilogue.refillFromCloudTile(gameLobby, player.get(0), 0));
        gameLobby.setCurrentPlayer(player.get(0));
        assertThrows(NoCloudTileException.class, () -> turnEpilogue.refillFromCloudTile(gameLobby, player.get(0), gameModel.getCloudTiles().size()));
        gameModel.getCloudTile(0).getAndRemoveStudents();
        assertThrows(EmptyStudentListException.class, () -> turnEpilogue.refillFromCloudTile(gameLobby, player.get(0), 0));
    }
}
