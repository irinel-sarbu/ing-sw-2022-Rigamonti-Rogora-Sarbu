package controller.server;

import events.*;
import model.GameModel;
import view.View;

import java.util.logging.Logger;

public class GameController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final GameModel model;
    private final View view;

    public GameController(GameModel gameModel, View view) {
        this.model = gameModel;
        this.view = view;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
    }

}
