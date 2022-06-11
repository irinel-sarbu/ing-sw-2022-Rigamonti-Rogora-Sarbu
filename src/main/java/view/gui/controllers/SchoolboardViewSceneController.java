package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Player;
import model.board.SchoolBoard;
import network.LightModel;
import util.Color;
import util.GameMode;
import view.gui.SceneController;

import java.util.List;
import java.util.Objects;

public class SchoolboardViewSceneController implements GenericSceneController {
    LightModel model;

    @FXML
    private AnchorPane fullScene, board2;

    /**
     * return to previous scene
     * @param mouseEvent
     */
    @FXML
    public void onBack(MouseEvent mouseEvent){
        SceneController.switchScene("genericMenuScene.fxml");
        GenericMenuSceneController controller = (GenericMenuSceneController) SceneController.getCurrentSceneController();
        if (model.getPlayerName().equals(model.getCurrentPlayerName())) {
            controller.setController(model, model.getPlayerName());
        } else {
            controller.setIdle(model, model.getCurrentPlayerName());
        }
    }

    /**
     * get node referring to the player's board
     * @param i ID of the player
     * @return
     */
    private List<Node> getBoard(int i) {
        return ((AnchorPane) fullScene.getChildren().get(i + 3)).getChildren();
    }

    /**
     * get node referring to the player's label name
     * @param i ID of the player
     * @return
     */
    private Label getName(int i) {
        return (Label) getBoard(i).get(1);
    }

    /**
     * get node referring to the player's towers field
     * @param i ID of the player
     * @return
     */
    private GridPane getTowers(int i) {
        return (GridPane) getBoard(i).get(2);
    }

    /**
     * get node referring to the player's coin
     * @param i ID of the player
     * @return
     */
    private AnchorPane getCoins(int i) {
        return (AnchorPane) getBoard(i).get(3);
    }

    /**
     * get node referring to the player's professors
     * @param i ID of the player
     * @return
     */
    private AnchorPane getProfessors(int i) {
        return (AnchorPane) getBoard(i).get(4);
    }

    /**
     * get node referring to the player's dining room
     * @param i ID of the player
     * @return
     */
    private AnchorPane getDining(int i) {
        return (AnchorPane) getBoard(i).get(5);
    }

    /**
     * get node referring to the color's table of the player's dining room
     * @param i ID of the player
     * @param color color of the table
     * @return
     */
    private GridPane getTable(int i, Color color) {
        return (GridPane) ((AnchorPane) getBoard(i).get(5)).getChildren().get(color.getValue());
    }

    /**
     * get node referring to the player's dining room
     * @param i ID of the player
     * @return
     */
    private GridPane getEntrance(int i) {
        return (GridPane) getBoard(i).get(6);
    }

    /**
     * Clear boards
     */
    private void resetBoards() {
        for (int b = 0; b < 3; b++) {
            fullScene.getChildren().get(b + 3).setVisible(false);
            // reset towers
            for (int i = 0; i < 8; i++) {
                getTowers(b).getChildren().get(i).setVisible(false);
            }
            // reset coins
            for (int j = 0; j < 2; j++) {
                getCoins(b).getChildren().get(j).setVisible(false);
            }
            // reset professors
            for (int j = 0; j < Color.values().length; j++) {
                getProfessors(b).getChildren().get(j).setVisible(false);
            }
            // reset dining
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 10; k++) {
                    ((GridPane) getDining(b).getChildren().get(j)).getChildren().get(k).setVisible(false);
                }
            }
            // reset entrance
            for (int j = 0; j < 9; j++) {
                getEntrance(b).getChildren().get(j).setVisible(false);
            }
        }
    }

    /**
     * Display board of the specified player
     * @param sb player's ID
     * @param name player's name
     */
    private void displayBoard(int sb, String name) {

        SchoolBoard schoolBoard = model.getSchoolBoardMap().get(name);
        fullScene.getChildren().get(sb + 3).setVisible(true);
        // display towers
        for (int i = 0; i < schoolBoard.getTowers().size(); i++) {
            getTowers(sb).getChildren().get(i).setVisible(true);
            ((ImageView) getTowers(sb).getChildren().get(i)).setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/Pedine/tower_" + schoolBoard.getTowers().get(0).getColor() + ".png"))));
        }
        // display coins
        if (model.getGameMode().equals(GameMode.EXPERT)) {
            if (schoolBoard.getCoinSupply().getNumOfCoins() > 0)
                getCoins(sb).getChildren().get(0).setVisible(true);
            if (schoolBoard.getCoinSupply().getNumOfCoins() > 1) {
                getCoins(sb).getChildren().get(1).setVisible(true);
                ((Label) getCoins(sb).getChildren().get(1)).setText("" + schoolBoard.getCoinSupply().getNumOfCoins());
            }
        }
        // display professors
        for (int i = 0; i < Color.values().length; i++) {
            if (schoolBoard.hasProfessor(Color.values()[i])) {
                getProfessors(sb).getChildren().get(i).setVisible(true);
            }
        }
        // display dining
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 10; k++) {
                ((GridPane) getDining(sb).getChildren().get(j)).getChildren().get(k).setVisible(
                        schoolBoard.getStudentsOfColor(Color.values()[j]) > k);
            }
        }
        // display entrance
        for (int i = 0; i < schoolBoard.getEntranceStudents().size(); i++) {
            getEntrance(sb).getChildren().get(i).setVisible(true);
            ((ImageView) getEntrance(sb).getChildren().get(i)).setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/Graphical_Assets/students/" + schoolBoard.getEntranceStudents().get(i).getColor() + "StudentResized.png"))));
        }
    }

    /**
     * Draw complete scene
     * @param model
     */
    public void updateView(LightModel model) {

        this.model = model;

        List<String> players = model.getSchoolBoardMap().keySet().stream().toList();

        resetBoards();

        for (int i = 0; i < players.size(); i++) {
            getName(i).setText(players.get(i));
            displayBoard(i, players.get(i));
        }
    }
}
