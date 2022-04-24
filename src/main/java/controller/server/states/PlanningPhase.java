package controller.server.states;

import controller.server.GameLobby;
import events.types.Messages;
import events.types.serverToClient.EPlayerChoseAssistant;
import events.types.serverToClient.Message;
import events.types.serverToClient.gameStateEvents.EUpdateAssistantDeck;
import exceptions.AssistantNotInDeckException;
import exceptions.WrongPhaseException;
import exceptions.WrongPlayerException;
import model.Player;
import model.board.Assistant;
import network.server.ClientSocketConnection;
import util.GameState;

import java.util.ArrayList;
import java.util.List;

public class PlanningPhase {
    private List<Assistant> playedAssistants;

    public PlanningPhase() {
        playedAssistants = new ArrayList<>();
    }

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
     * Player plays a card, moving that card from own hand deck to fold deck
     *
     * @param thisGame      current lobby
     * @param actingPlayer  player performing the action
     * @param assistantCard assistant card the player wants to play
     * @throws WrongPhaseException         the game is not in the planning phase
     * @throws WrongPlayerException        the player doesn't have right to play at the moment
     * @throws AssistantNotInDeckException the selected assistant does not exist in the player's hand deck
     */
    public void playCard(GameLobby thisGame, Player actingPlayer, Assistant assistantCard, ClientSocketConnection client)
            throws WrongPhaseException, WrongPlayerException, AssistantNotInDeckException {
        if (thisGame.wrongState(GameState.PLANNING)) throw new WrongPhaseException();
        if (thisGame.wrongPlayer(actingPlayer)) throw new WrongPlayerException();
        if (checkIfAssistantPlayed(actingPlayer, assistantCard)) {
            client.send(new Message(Messages.INVALID_ASSISTANT));
            return;
        }

        thisGame.getCurrentPlayer().pushFoldDeck(
                thisGame.getCurrentPlayer().removeCard(assistantCard));
        client.send(new EUpdateAssistantDeck(thisGame.getCurrentPlayer().getAssistants()));
        playedAssistants.add(assistantCard);

        thisGame.broadcastExceptOne(new EPlayerChoseAssistant(thisGame.getPlayerNameBySocket(client), assistantCard), thisGame.getPlayerNameBySocket(client));
        if (!thisGame.setNextPlayer()) {
            computeNext(thisGame);
            playedAssistants.clear();
        }
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

    private boolean checkIfAssistantPlayed(Player player, Assistant assistant) {
        if (player.getAssistants().size() <= playedAssistants.size()) return false;
        for (Assistant assistantCard : playedAssistants) {
            if (assistantCard.equals(assistant)) return true;
        }
        return false;
    }
}
