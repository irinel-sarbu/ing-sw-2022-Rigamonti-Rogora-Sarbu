package controller;

import model.Game;
import view.View;

import java.util.Observable;
import java.util.Observer;

public class GameController implements Observer {

    private final Game model;
    private final View view;

    public GameController(Game gameModel, View view) {
        this.model = gameModel;
        this.view = view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
