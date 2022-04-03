package controller.server.states;

import controller.server.GameController;
import controller.server.GameLobby;
import exceptions.*;
import model.GameModel;
import model.Player;
import model.board.Assistant;
import util.GameState;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
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
        if (thisGame.wrongState(GameState.PLANNING)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(actingPlayer)) throw new WrongPlayerException();
        if (!playable(thisGame, assistantCard)) throw new NotPlayableAssistantException();

        thisGame.getCurrentPlayer().pushFoldDeck(
                thisGame.getCurrentPlayer().removeCard(assistantCard));

        if (thisGame.setNextPlayer()) computeNext(thisGame);
    }

    private void computeNext(GameLobby thisGame) throws WrongPhaseException {
        if (thisGame.wrongState(GameState.PLANNING)) throw new WrongPhaseException();
        /* if (thisGame.getOrder().stream().map(Player::peekFoldDeck).filter(Objects::nonNull).count() != // in case of disconnection prevent game progress
                thisGame.getOrder().size()) throw new WrongPhaseException(); */
        List<Player> nextOrder = new ArrayList<>(thisGame.getOrder());
        nextOrder.sort(Player::compareTo);
        thisGame.setOrder(nextOrder);

        thisGame.setGameState(GameState.STUDENT_MOVEMENT);
    }

}
