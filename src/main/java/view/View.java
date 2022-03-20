package view;

import events.EventListener;
import events.EventSender;
import java.util.logging.Logger;

public abstract class View extends EventSender implements EventListener {
    protected final Logger LOGGER = Logger.getLogger(View.class.getName());

    public abstract void run();
}
