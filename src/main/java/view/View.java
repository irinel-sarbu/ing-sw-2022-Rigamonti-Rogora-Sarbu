package view;

import events.EventListener;
import events.EventSender;

public abstract class View extends EventSender {
    public abstract void run();
    public void displayMessage(String message) {
        System.out.println("> " + message);
    }

    public void displayError(String errorMessage) {
        System.out.println("ERROR > " + errorMessage);
    }

    public abstract void getServerInfo();
    public abstract void getPlayerName(String lobbyToJoin);
    public abstract void chooseCreateOrJoin();
}
