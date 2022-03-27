package controller.server.states;

import model.GameModel;
import model.board.Assistant;

import java.util.HashMap;

public class PlanningPhase {

    public void refillEmptyClouds(GameModel game) {
        for (int i = 0; i < game.getNumOfCloudTiles(); i++)
            game.refillCloudTile(i);
    }

}
