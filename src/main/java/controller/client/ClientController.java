package controller.client;

import controller.server.GameController;
import events.*;
import model.GameModel;
import view.View;

import java.util.logging.Logger;

public class ClientController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final GameModel model;
    private final View view;

    public ClientController(GameModel gameModel, View view) {
        this.model = gameModel;
        this.view = view;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
    }
}
