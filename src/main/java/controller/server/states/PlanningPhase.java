package controller.server.states;

import controller.server.GameLobby;
import exceptions.*;
import model.GameModel;
import model.Player;
import model.board.Assistant;
import util.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlanningPhase {

    /**
     * Refill clouds with students
     *
     * @param thisGame current game lobby
     */
    public void refillEmptyClouds(GameLobby thisGame) {
        for (int i = 0; i < thisGame.getModel().getNumOfCloudTiles(); i++)
            thisGame.getModel().refillCloudTile(i);
    }

    /**
     * Check if an assistant can be played by the specified player
     *
     * @param thisGame      game lobby
     * @param assistantCard the card to play
     * @return true if the card can be played by the current player, false otherwise
     */
    private boolean playable(GameLobby thisGame, Assistant assistantCard) {
        List<Assistant> played = thisGame.getOrder().stream()
                .map(Player::peekFoldDeck)
                .filter(Objects::nonNull).collect(Collectors.toList());

        return !played.contains(assistantCard) ||                                                  // unique card
                !thisGame.getCurrentPlayer().getAssistants().stream()                               // and no other options
                        .filter(assistant -> !assistant.equals(assistantCard)).allMatch(played::contains);

    }

    /**
     * Player plays a card, moving that card from own hand deck to fold deck
     *
     * @param thisGame      current lobby
     * @param actingPlayer  player performing the action
     * @param assistantCard assistant card the player wants to play
     * @throws WrongPhaseException           the game is not in the planning phase
     * @throws WrongPlayerException          the player doesn't have right to play at the moment
     * @throws NotPlayableAssistantException the selected assistant cannot be played
     * @throws AssistantNotInDeckException   the selected assistant does not exist in the player's hand deck
     */
    public void playCard(GameLobby thisGame, Player actingPlayer, Assistant assistantCard)
            throws WrongPhaseException, WrongPlayerException, NotPlayableAssistantException, AssistantNotInDeckException {
        if (thisGame.wrongState(GameState.PLANNING)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(actingPlayer)) throw new WrongPlayerException();
        if (!playable(thisGame, assistantCard)) throw new NotPlayableAssistantException();

        thisGame.getCurrentPlayer().pushFoldDeck(
                thisGame.getCurrentPlayer().removeCard(assistantCard));

        if (!thisGame.setNextPlayer()) computeNext(thisGame);
    }

    /**
     * Compute next phase order and switch phase to student movement
     *
     * @param thisGame current game lobby
     * @throws WrongPhaseException the game is not in the current phase (should never happen)
     */
    private void computeNext(GameLobby thisGame) throws WrongPhaseException {
        if (thisGame.wrongState(GameState.PLANNING)) throw new WrongPhaseException();
        /* if (thisGame.getOrder().stream().map(Player::peekFoldDeck).filter(Objects::nonNull).count() != // TODO: in case of disconnection prevent game progress
                thisGame.getOrder().size()) throw new WrongPhaseException(); */
        List<Player> nextOrder = new ArrayList<>(thisGame.getOrder());
        nextOrder.sort(Player::compareTo);
        thisGame.setOrder(nextOrder);

        thisGame.setGameState(GameState.STUDENT_MOVEMENT);
    }

}
