package view;

import network.LightModel;
import observer.Observable;
import util.Wizard;

import java.util.List;

public abstract class View extends Observable {
    public abstract void run();
    public void displayMessage(String message) {
        System.out.println("> " + message);
    }

    public void displayError(String errorMessage) {
        System.out.println("ERROR > " + errorMessage);
    }

    public abstract void askServerInfo();

    public abstract void createLobby();

    public abstract void joinLobby();

    public abstract void chooseCreateOrJoin();

    public abstract void chooseWizard(List<Wizard> availableWizards);

    public abstract void update(LightModel model);
}