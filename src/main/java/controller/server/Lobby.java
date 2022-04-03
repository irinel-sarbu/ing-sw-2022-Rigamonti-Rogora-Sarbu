package controller.server;

import events.*;
import events.types.clientToServer.EWizardChosen;
import model.*;
import observer.NetworkObserver;
import events.types.Messages;
import events.types.serverToClient.*;
import network.server.ClientSocketConnection;
import util.GameMode;
import util.Logger;
import util.Tuple;
import util.Wizard;

import java.util.*;

public class Lobby implements NetworkObserver {

    private final String lobbyCode;
    private final int maxPlayers;
    private final GameMode gameMode;
    private final Map<String, ClientSocketConnection> clientList;

    private final List<Wizard> availableWizards;

    private final GameModel model;

    public Lobby(String code, int maxPlayers, GameMode gameMode) {
        this.lobbyCode = code;
        this.maxPlayers = maxPlayers;
        this.gameMode = gameMode;

        this.clientList = new HashMap<>();
        this.model = new GameModel(maxPlayers, this.gameMode);
        this.availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));
    }

    @Override
    public void onNetworkEvent(Tuple<Event, ClientSocketConnection> networkEvent) {
        EventDispatcher dp = new EventDispatcher(networkEvent);

        dp.dispatch(EventType.WIZARD_CHOSEN, (Tuple<Event, ClientSocketConnection> t) -> playerHasChosenWizard((EWizardChosen) t.getKey(), t.getValue()));
    }

    /**
     * Broadcast Event to all clients connected to lobby
     *
     * @param event Event to broadcast
     */
    public void broadcast(Event event) {
        for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
            ClientSocketConnection client = entry.getValue();
            client.asyncSend(event);
        }
    }

    /**
     * Broadcast Event to all clients connected to lobby, except sender
     *
     * @param event          Event to broadcast
     * @param excludedClient Client to exclude
     */
    public void broadcastExceptOne(Event event, String excludedClient) {
        for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
            if (entry.getKey() != null && !entry.getKey().equals(excludedClient)) {
                ClientSocketConnection client = entry.getValue();
                client.asyncSend(event);
            }
        }
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void addClientToLobby(String name, ClientSocketConnection client) {
        if(model.getPlayerByName(name) != null && model.getPlayerByName(name).isDisconnected()) {
            clientList.put(name, client);
            client.joinLobby(lobbyCode);
            model.getPlayerByName(name).setDisconnected(false);
            broadcastExceptOne(new PlayerJoined(name), name);
            Logger.info("Player " + name + " reconnected to Lobby " + getLobbyCode());
            return;
        }

        if (model.getPlayerSize() >= maxPlayers) {
            client.asyncSend(new Message(Messages.LOBBY_FULL));
            Logger.info("Player " + name + " trying to connect but lobby is full.");
            return;
        }

        if (getClientByName(name) != null) {
            client.asyncSend(new Message(Messages.NAME_NOT_AVAILABLE));
            Logger.info("Player " + name + " trying to connect but lobby there is already a player with that name connected.");
            return;
        }

        clientList.put(name, client);
        client.joinLobby(lobbyCode);
        client.asyncSend(new LobbyJoined(lobbyCode));
        broadcastExceptOne(new PlayerJoined(name), name);
        client.asyncSend(new EChooseWizard(new ArrayList<>(availableWizards)));
    }

    public void removeClientFromLobbyByName(String name) {
        clientList.remove(name);
        if(model.getPlayerByName(name) != null)
            model.getPlayerByName(name).setDisconnected(true);
    }

    public ClientSocketConnection getClientByName(String name) {
        return clientList.get(name);
    }

    public String getClientBySocket(ClientSocketConnection clientSocket) {
        for (Map.Entry<String, ClientSocketConnection> client : clientList.entrySet()) {
            if (client.getValue().equals(clientSocket)) {
                return client.getKey();
            }
        }
        return null;
    }

    public boolean playerHasChosenWizard(EWizardChosen event, ClientSocketConnection client) {
        Wizard choice = event.getWizard();

        if (!availableWizards.contains(choice)) {
            client.asyncSend(new EWizardNotAvailable(availableWizards));
            return true;
        }

        String playerName = getClientBySocket(client);
        Logger.debug("Lobby " + getLobbyCode() + " - Adding " + playerName + " [" + choice + "] to board.");
        model.addPlayer(new Player(playerName, choice));
        availableWizards.remove(choice);

        checkReadyPlayers();
        return true;
    }

    private void checkReadyPlayers() {
        if(model.getNumOfPlayers() == maxPlayers) {
            broadcast(new Message(Messages.GAME_STARTED));
        }
    }
}