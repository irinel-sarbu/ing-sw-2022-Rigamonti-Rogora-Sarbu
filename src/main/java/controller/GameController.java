package controller;

import events.*;
import events.types.SimpleMessageEvent;
import model.Game;
import view.View;

import java.util.logging.Logger;

public class GameController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final Game model;
    private final View view;

    public GameController(Game gameModel, View view) {
        this.model = gameModel;
        this.view = view;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);

        dispatcher.dispatch(EventType.SIMPLE_MESSAGE, (Event e) -> (onSimpleMessage((SimpleMessageEvent) e)));
    }

    private boolean onSimpleMessage(SimpleMessageEvent event) {
        LOGGER.info("New message: " + event.getMessage());
        return true;
    }

}
