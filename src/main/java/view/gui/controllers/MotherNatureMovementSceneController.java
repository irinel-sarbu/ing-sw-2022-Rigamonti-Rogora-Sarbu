package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.actionPhaseRelated.EMoveMotherNature;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToIsland;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.board.IslandGroup;
import model.board.IslandTile;
import network.LightModel;
import util.CharacterType;
import util.Color;
import util.TowerColor;

import java.util.ArrayList;
import java.util.List;

public class MotherNatureMovementSceneController implements GenericSceneController {
    LightModel model;

    String pathPrefix = "/Graphical_Assets/";
    String studentSuffix = "StudentResized.png";

    @FXML
    private AnchorPane bridges_parent;
    @FXML
    private Pane stepsButtons;
    @FXML
    private GridPane realm_parent;
    @FXML
    private Button stepButton0, stepButton1, stepButton2, stepButton3, stepButton4, stepButton5, stepButton6;

    private List<Node> bridges;
    private List<Node> islands;
    private List<Node> stepButtonsList;

    @FXML
    public void onStepButton0(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton0.getText())));
    }

    @FXML
    public void onStepButton1(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton1.getText())));
    }

    @FXML
    public void onStepButton2(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton2.getText())));
    }

    @FXML
    public void onStepButton3(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton3.getText())));
    }

    @FXML
    public void onStepButton4(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton4.getText())));
    }

    @FXML
    public void onStepButton5(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton5.getText())));
    }

    @FXML
    public void onStepButton6(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton6.getText())));
    }

    private void init(LightModel model) {
        this.model = model;
        bridges = new ArrayList<>(bridges_parent.getChildren());
        islands = realm_parent.getChildren().subList(0, 12);
        stepButtonsList = new ArrayList<>(stepsButtons.getChildren());
    }

    private int groupByIslandID(int ID) {
        for (IslandGroup ig : model.getIslandGroups()) {
            if (ig.getIslands().stream().map(IslandTile::getIslandID).toList().contains(ID))
                return ig.getIslandGroupID();
        }
        return -1;
    }

    private boolean inSameGroup(int a, int b) {
        return groupByIslandID(a) == groupByIslandID(b);
    }

    // Display functions
    private void updateBridge() {
        for (int i = 0; i < 12; i++) {
            if (inSameGroup(i, (i + 1) % 12)) bridges.get(i).setVisible(true);
        }
    }

    private void updateStudents(int island, Color color, int number) {
        AnchorPane studentPane = (AnchorPane) ((AnchorPane) ((AnchorPane) islands.get(island)).getChildren().get(4)).getChildren().get(color.getValue());
        if (number == 0) {
            ((ImageView) (studentPane.getChildren().get(0))).setImage(new Image(pathPrefix + "cerchi.png"));
            ((studentPane.getChildren().get(1))).setVisible(false);
            return;
        }
        ((ImageView) (studentPane.getChildren().get(0))).setImage(new Image(pathPrefix + "students/" + color + studentSuffix));
        if (number > 1) {
            ((studentPane.getChildren().get(1))).setVisible(true);
            ((Label) (studentPane.getChildren().get(1))).setText("" + number);
        }
    }

    private void updateNoEntry(int island, int number) {
        AnchorPane noEntryPane = (AnchorPane) (((AnchorPane) islands.get(island)).getChildren().get(1));
        if (number > 0) {
            noEntryPane.setVisible(true);
        }
        if (number > 1) {
            ((noEntryPane.getChildren().get(1))).setVisible(true);
            ((Label) (noEntryPane.getChildren().get(1))).setText("" + number);
        }
    }

    private void updateTower(int island, TowerColor color) {
        ImageView towerPane = (ImageView) (((AnchorPane) islands.get(island)).getChildren().get(2));
        towerPane.setVisible(color != null);
        towerPane.setImage(new Image(pathPrefix + "Pedine/tower_" + color + ".png"));
    }

    private void updateMotherNature(int position) {
        for (int i = 0; i < 12; i++) {
            ImageView motherNature = ((ImageView) ((AnchorPane) islands.get(i)).getChildren().get(3));
            motherNature.setVisible(i == position);
        }
    }

    private int toInt(String string) {
        return Integer.parseInt(string);
    }

    public void setupIslands(LightModel model) {
        init(model);
        updateBridge();

        List<IslandTile> allIslands = new ArrayList<>();
        for (IslandGroup ig : model.getIslandGroups()) {
            allIslands.addAll(ig.getIslands());
        }

        // update students view
        for (IslandTile it : allIslands) {
            for (Color c : Color.values()) {
                updateStudents(it.getIslandID(), c, it.getStudentsNumber(c));
            }
        }

        // update noEntry
        for (IslandGroup ig : model.getIslandGroups()) {
            for (IslandTile it : ig.getIslands()) {
                updateNoEntry(it.getIslandID(), ig.getNoEntrySize());
            }
        }

        // update Towers
        for (IslandTile it : allIslands) {
            for (Color c : Color.values()) {
                updateTower(it.getIslandID(), it.getTowerColor());
            }
        }

        // update motherNature
        updateMotherNature(model.getMotherNaturePosition());

        int maxMovements;

        if (model.getActiveCharacterEffect() == CharacterType.POSTMAN) {
            maxMovements = model.getChosenAssistant().getMovements() + 2;
        } else {
            maxMovements = model.getChosenAssistant().getMovements();
        }

        for (int i = 0; i < maxMovements; i++) {
            Button button = (Button) stepButtonsList.get(i);
            button.setVisible(true);
        }

        stepsButtons.setVisible(true);
    }
}
