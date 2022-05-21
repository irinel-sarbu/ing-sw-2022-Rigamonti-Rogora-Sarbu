package controller.server.states;

import controller.server.GameLobby;
import exceptions.AssistantAlreadyPlayedException;
import exceptions.AssistantNotInDeckException;
import exceptions.WrongPhaseException;
import exceptions.WrongPlayerException;
import model.GameModel;
import model.Player;
import model.board.Assistant;
import model.board.Bag;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlanningPhaseTest {
    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static List<Player> player;
    private static Server server;
    private static Socket socket;
    private static UUID uuid;
    private static ClientHandler clientSocketConnection;

    private static PlanningPhase planningPhase;

    @BeforeEach
    public void planningPhase() {

        server = new Server(5000);
        socket = new Socket();
        try {
            clientSocketConnection = new ClientHandler(server, socket);
        } catch (IOException e) {
            System.out.println("test");
        }

        gameLobby = new GameLobby(3, GameMode.NORMAL, "00000", new Server(5000));

        gameModel = gameLobby.getModel();
        for (int i = 0; i < 3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.NORMAL));
        }
        gameLobby.setOrder(gameModel.getPlayers());
        gameLobby.setGameState(GameState.PLANNING);
        player = new ArrayList<>(gameLobby.getOrder());
        gameLobby.setCurrentPlayer(player.get(0));
        planningPhase = new PlanningPhase();
    }

    @Test
    public void refillEmptyClouds() {

        Bag bag = gameModel.getBag();

        int size = bag.getRemainingStudents();

        for(int i=0; i<gameModel.getNumOfCloudTiles(); i++) {
            gameModel.getCloudTiles().get(i).getAndRemoveStudents();
        }

        for (int i = 0; i < gameModel.getNumOfCloudTiles(); i++) {
            assertTrue(gameModel.getCloudTiles().get(i).isEmpty());
        }

        planningPhase.refillEmptyClouds(gameLobby);

        assertEquals(size, bag.getRemainingStudents() + gameModel.getNumOfCloudTiles() * (gameModel.getMaxNumOfPlayers() + 1));
    }

    @Test
    public void playCard() {

            //  test wrong phase
        gameLobby.setGameState(GameState.MOTHERNATURE_MOVEMENT);
        try {
            planningPhase.playCard(gameLobby, player.get(0), player.get(0).getAssistants().get(0), clientSocketConnection);
            System.err.println("WrongPhaseException expected");
            fail();
        } catch (WrongPhaseException e) {
            // assert true
        } catch (Exception e) {
            System.err.println("Expected WrongPhaseException, raised: " + e);
            fail();
        }

        //  test wrong player
        gameLobby.setGameState(GameState.PLANNING);
        gameLobby.setCurrentPlayer(player.get(0));
        try {
            planningPhase.playCard(gameLobby, player.get(1), player.get(1).getAssistants().get(0), clientSocketConnection);
            System.err.println("Expected WrongPlayerException, none raised");
            fail();
        } catch (WrongPlayerException e) {
            // assert true
        } catch (Exception e) {
            System.err.println("Expected WrongPlayerException, raised: " + e);
            fail();
        }

        //  test correct play
        try {
            planningPhase.playCard(gameLobby, player.get(0), player.get(0).getAssistants().get(0), clientSocketConnection);
        } catch (Exception e) {
            System.err.println("Player 0 playing");
            System.err.println(e + " raising not expected");
            fail();
        }

        //  test assistant played
        int preSize = player.get(1).getAssistants().size();
        try {
            assertThrows(AssistantAlreadyPlayedException.class, () -> planningPhase.playCard(gameLobby, player.get(1), player.get(1).getAssistants().get(0), clientSocketConnection));
            assertEquals(preSize, player.get(1).getAssistants().size());
        } catch (Exception other) {
            System.err.println(other + " not expected");
            fail();
        }
        try {
            planningPhase.playCard(gameLobby, player.get(1), player.get(1).getAssistants().get(1), clientSocketConnection);
        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }

        try {
            planningPhase.playCard(gameLobby, player.get(2), player.get(2).getAssistants().get(2), clientSocketConnection);

            assertEquals(GameState.STUDENT_MOVEMENT, gameLobby.getCurrentGameState());

            for (int i = 0; i < 3; i++) {
                assertEquals(player.get(i), gameLobby.getOrder().get(i));
            }

            gameLobby.nextRound();
            assertEquals(GameState.PLANNING, gameLobby.getCurrentGameState());
            Collections.reverse(player);
            for (int i = 0; i < 3; i++) {
                assertEquals(player.get(i), gameLobby.getOrder().get(i));
            }

        } catch (Exception e) {
            System.err.println("Player 1 playing");
            System.err.println("Exception raising not expected");
            fail();
        }
    }

    @Test
    public void computeNext() {
        List<Player> nextOrder = new ArrayList<>(gameLobby.getOrder());
        nextOrder.sort(Player::compareTo);

        for (int i = 0; i < 3; i++) {
            assertEquals(nextOrder.get(i), gameLobby.getOrder().get(i));
        }
    }

    @Test
    public void canBePlayedFirst() {
        //  use some cards
        int size = player.get(0).getAssistants().size();
        for (int i = 3; i < size; i++) {
            try {
                for (Player value : player) {
                    value.removeCard(0);
                }
            } catch (AssistantNotInDeckException e) {
                System.err.println(e + " not expected");
                fail();
            }
        }

        try {
            planningPhase.playCard(gameLobby, player.get(0), player.get(0).getAssistants().get(0), clientSocketConnection);
            planningPhase.playCard(gameLobby, player.get(1), player.get(1).getAssistants().get(1), clientSocketConnection);

            assertTrue(planningPhase.checkIfAssistantPlayed(player.get(2), player.get(2).getAssistants().get(0)));
            int size1 = player.get(2).getAssistants().size();
            assertThrows(AssistantAlreadyPlayedException.class, () -> planningPhase.playCard(gameLobby, player.get(2), player.get(0).getAssistants().get(0), clientSocketConnection));
            assertEquals(size1, player.get(2).getAssistants().size());

            assertFalse(planningPhase.checkIfAssistantPlayed(player.get(2), player.get(2).getAssistants().get(2)));
            player.get(2).removeCard(2);
            assertFalse(planningPhase.checkIfAssistantPlayed(player.get(2), player.get(2).getAssistants().get(0)));

        } catch (Exception e) {
            System.err.println(e + " not expected");
            fail();
        }
    }
}
