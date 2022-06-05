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

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton0(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton0.getText())));
    }

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton1(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton1.getText())));
    }

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton2(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton2.getText())));
    }

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton3(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton3.getText())));
    }

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton4(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton4.getText())));
    }

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton5(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton5.getText())));
    }

    /**
     * notify mother nature's new position
     * @param mouseEvent
     */
    @FXML
    public void onStepButton6(MouseEvent mouseEvent) {
        EventManager.notify(new EMoveMotherNature(toInt(stepButton6.getText())));
    }

    /**
     * setup view variables
     * @param model
     */
    private void init(LightModel model) {
        this.model = model;
        bridges = new ArrayList<>(bridges_parent.getChildren());
        islands = realm_parent.getChildren().subList(0, 12);
        stepButtonsList = new ArrayList<>(stepsButtons.getChildren());
    }

    /**
     * get the ID of the group containing the island
     * @param ID group ID
     * @return island ID
     */
    private int groupByIslandID(int ID) {
        for (IslandGroup ig : model.getIslandGroups()) {
            if (ig.getIslands().stream().map(IslandTile::getIslandID).toList().contains(ID))
                return ig.getIslandGroupID();
        }
        return -1;
    }

    /**
     * check if two island are in the same group
     * @param a ID of first island
     * @param b ID of second island
     * @return {@link true} if the two island are in the same group, {@link false} otherwise
     */
    private boolean inSameGroup(int a, int b) {
        return groupByIslandID(a) == groupByIslandID(b);
    }

    /**
     * update bridges visibility
     */
    // Display functions
    private void updateBridge() {
        for (int i = 0; i < 12; i++) {
            if (inSameGroup(i, (i + 1) % 12)) bridges.get(i).setVisible(true);
        }
    }

    /**
     * update students visibility of a color inside island
     * @param island ID of the island to update
     * @param color {@link Color} enum entry of the students to draw
     * @param number number of students to draw
     */
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

    /**
     * update no entry visibility on an island
     * @param island ID of the island to update
     * @param number number of no entry tile
     */
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

    /**
     * Update tower visibility
     * @param island ID of the island to update
     * @param color {@link TowerColor} enum entry of the tower
     */
    private void updateTower(int island, TowerColor color) {
        ImageView towerPane = (ImageView) (((AnchorPane) islands.get(island)).getChildren().get(2));
        towerPane.setVisible(color != null);
        towerPane.setImage(new Image(pathPrefix + "Pedine/tower_" + color + ".png"));
    }

    /**
     * Draw mother nature on the specified island
     * @param position ID of the island containing mother nature
     */
    private void updateMotherNature(int position) {
        for(int i=0; i<12; i++) {
            ImageView motherNature = ((ImageView) ((AnchorPane) islands.get(i)).getChildren().get(3));
            motherNature.setVisible(i==model.getIslandGroups().get(groupByIslandID(position)).getIslands().get(0).getIslandID());
        }
    }

    /**
     * Convert String to int
     * @param string string
     * @return int
     */
    private int toInt(String string) {
        return Integer.parseInt(string);
    }

    /**
     * Display all infos about the island
     * @param model reference to light model
     */
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
        int position = model.getIslandGroups().get(model.getMotherNaturePosition()).getIslands().get(0).getIslandID();
        updateMotherNature(position);

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
