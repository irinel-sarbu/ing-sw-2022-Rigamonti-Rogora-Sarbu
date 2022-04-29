package controller.server.states;

import controller.server.GameLobby;
import model.GameModel;
import model.Player;
import model.board.Bag;
import model.board.CloudTile;
import network.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.GameMode;
import util.TowerColor;
import util.Wizard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlanningPhaseTest {
    private static GameLobby gameLobby;
    private static GameModel gameModel;
    private static Player[] player;

    private static PlanningPhase planningPhase;

    @BeforeEach
    public void planningPhase() {
        gameLobby = new GameLobby(3, GameMode.NORMAL, "00000", new Server());
        gameModel = gameLobby.getModel();
        player = new Player[3];
        for(int i=0; i<3; i++) {
            gameModel.addPlayer(new Player("player" + i, Wizard.values()[i], TowerColor.values()[i], GameMode.NORMAL));
        }
        planningPhase = new PlanningPhase();
    }

    @Test
    public void refillEmptyClouds() {

        Bag bag = gameModel.getBag();
        int size = bag.getRemainingStudents();

        for(int i=0; i<gameModel.getNumOfCloudTiles(); i++) {
            gameModel.getCloudTiles().get(i).getAndRemoveStudents();
        }

        for(int i=0; i< gameModel.getNumOfCloudTiles(); i++) {
            assertTrue(gameModel.getCloudTiles().get(i).isEmpty());
        }

        planningPhase.refillEmptyClouds(gameLobby);

        //assertEquals(size, bag.getRemainingStudents() + gameModel.getNumOfCloudTiles() * gameModel.getNumOfCloudTiles() * (gameModel.getMaxNumOfPlayers()+1));

    }
}
