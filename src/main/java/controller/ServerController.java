package controller;

import events.*;
import model.GameModel;
import network.Server;

import java.util.logging.Logger;

public class ServerController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(ServerController.class.getName());

    private final GameModel model;
    private final Server server;

    public ServerController(GameModel gameModel, Server server) {
        this.model = gameModel;
        this.server = server;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        //dispatcher.dispatch(EventType.SIMPLE_MESSAGE, (Event e) -> (onSimpleMessage((SimpleMessageEvent) e)));
    }
}
