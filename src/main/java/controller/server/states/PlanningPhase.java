package controller.server.states;

import controller.server.GameController;
import controller.server.GameLobby;
import exceptions.*;
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

    private boolean playable(GameLobby thisGame, Assistant assistantCard) {

        List<Assistant> played = thisGame.getOrder().stream()
                .map(Player::peekFoldDeck)
                .filter(Objects::nonNull).collect(Collectors.toList());

        return !played.contains(assistantCard) ||                                                  // unique card
                !thisGame.getCurrentPlayer().getAssistants().stream()                               // and no other options
                        .filter(assistant -> !assistant.equals(assistantCard)).allMatch(played::contains);

    }

    public void playCard(GameLobby thisGame, Player actingPlayer, Assistant assistantCard)
            throws WrongPhaseException, WrongPlayerException, NotPlayableAssistantException, AssistantNotInDeckException {
        if (thisGame.getCurrentGameState() != GameState.PLANNING) throw new WrongPhaseException();
        if (thisGame.getCurrentPlayer() != actingPlayer) throw new WrongPlayerException();
        if (!playable(thisGame, assistantCard)) throw new NotPlayableAssistantException();

        thisGame.getCurrentPlayer().pushFoldDeck(
                thisGame.getCurrentPlayer().removeCard(assistantCard));
    }

}
