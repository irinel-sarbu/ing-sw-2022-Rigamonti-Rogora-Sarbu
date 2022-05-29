package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Player;
import model.board.SchoolBoard;
import network.LightModel;
import util.Color;

import java.util.List;

public class SchoolboardViewSceneController implements GenericSceneController {
    LightModel model;

    @FXML
    private AnchorPane fullScene;

    private List<Node> getBoard(int i) {
        return ((AnchorPane)fullScene.getChildren().get(i + 1)).getChildren();
    }

    private Label getName(int i) {
        return (Label) getBoard(i).get(1);
    }

    private GridPane getTowers(int i) {
        return (GridPane) getBoard(i).get(2);
    }

    private AnchorPane getCoins(int i) {
        return (AnchorPane) getBoard(i).get(3);
    }

    private AnchorPane getProfessors(int i) {
        return (AnchorPane) getBoard(i).get(4);
    }

    private AnchorPane getDining(int i) {
        return (AnchorPane) getBoard(i).get(5);
    }

    private GridPane getTable(int i, Color color) {
        return (GridPane) ((AnchorPane)getBoard(i).get(5)).getChildren().get(color.getValue());
    }

    private GridPane getEntrance(int i) {
        return (GridPane) getBoard(i).get(6);
    }

    private void resetTower() {

        for(int b=0; b<3; b++) {
            for (int i = 0; i < 8; i++) {
                getTowers(b).getChildren().get(i).setVisible(false);
            }
        }
    }

    private void displayTower(int sb, String name) {

        SchoolBoard schoolBoard = model.getSchoolBoardMap().get(name);

        for(int i=0; i < schoolBoard.getTowers().size(); i++) {
            getTowers(sb).getChildren().get(i).setVisible(true);
            ((ImageView)getTowers(sb).getChildren().get(i)).setImage(new Image("/Graphical_Assets/Pedine/tower_" + schoolBoard.getTowers().get(0).getColor() + ".png"));
        }
    }

    void updateView(LightModel model) {

        this.model = model;

        List<String> players = model.getSchoolBoardMap().keySet().stream().toList();

        resetTower();

        // setting name
        for(int i=0; i<players.size(); i++){
            getName(i).setText(players.get(i));
        }

        // displaying towers
        for(int i=0; i<players.size(); i++) {
            displayTower(i, players.get(i));
        }
    }
}
