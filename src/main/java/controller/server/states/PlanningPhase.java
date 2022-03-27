package controller.server.states;

import controller.server.GameController;
import controller.server.GameLobby;
import exceptions.GameNotFoundException;
import exceptions.WrongPhaseException;
import exceptions.WrongPlayerException;
import model.GameModel;
import model.Player;
import model.board.Assistant;
import util.GameState;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlanningPhase {

    public void refillEmptyClouds(GameModel game) {
        for (int i = 0; i < game.getNumOfCloudTiles(); i++)
            game.refillCloudTile(i);
    }

    private boolean otherOptions(GameLobby thisGame, Assistant assistantCard) {

        List<Assistant> played = thisGame.getOrder().stream()
                .map(Player::peekFoldDeck)
                .filter(Objects::nonNull).collect(Collectors.toList());

        return !(thisGame.getCurrentPlayer().getAssistants().stream()
                .filter(assistant -> !assistant.equals(assistantCard)).allMatch(played::contains));
    }

    /*public void playCard(GameLobby thisGame, Player actingPlayer, Assistant assistantCard)
                        throws GameNotFoundException, WrongPhaseException, WrongPlayerException {
        if(thisGame.getCurrentGameState() != GameState.PLANNING) throw new WrongPhaseException();
        if(thisGame.getCurrentPlayer() != actingPlayer) throw new WrongPlayerException();
        List<Assistant> played = thisGame.getOrder().stream().map(Player::peekFoldDeck).collect(Collectors.toList());
            if (played.contains(assistantCard) && !otherOptions(thisGame, assistantCard)) {                             // TODO: contains -> equals
                throw new // TODO: non puoi giocarlo
            }
        }

    } broken */

}
