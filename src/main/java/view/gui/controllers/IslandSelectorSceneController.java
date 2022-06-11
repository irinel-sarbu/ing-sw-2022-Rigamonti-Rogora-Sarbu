package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToIsland;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import util.Color;
import util.TowerColor;
import view.gui.SceneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IslandSelectorSceneController implements GenericSceneController {
    LightModel model;

    String pathPrefix = "/Graphical_Assets/";
    String studentSuffix = "StudentResized.png";

    @FXML
    private AnchorPane bridges_parent;
    @FXML
    private Pane islandButtons;
    @FXML
    private GridPane realm_parent;

    private List<Node> bridges;
    private List<Node> islands;
    private int chosenStudentID;

    /**
     * notify island 0 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton0(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 0));
    }

    /**
     * notify island 1 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton1(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 1));
    }

    /**
     * notify island 2 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton2(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 2));
    }

    /**
     * notify island 3 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton3(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 3));
    }

    /**
     * notify island 4 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton4(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 4));
    }

    /**
     * notify island 5 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton5(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 5));
    }

    /**
     * notify island 6 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton6(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 6));
    }

    /**
     * notify island 7 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton7(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 7));
    }

    /**
     * notify island 8 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton8(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 8));
    }

    /**
     * notify island 9 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton9(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 9));
    }

    /**
     * notify island 10 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton10(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 10));
    }

    /**
     * notify island 11 selected
     * @param mouseEvent
     */
    @FXML
    public void onIslandButton11(MouseEvent mouseEvent) {
        EventManager.notify(new EStudentMovementToIsland(chosenStudentID, 11));
    }

    /**
     * setup scene infos
     * @param model
     */
    private void init(LightModel model) {
        this.model = model;
        bridges = new ArrayList<>(bridges_parent.getChildren());
        islands = realm_parent.getChildren().subList(0, 12);
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
            ((ImageView) (studentPane.getChildren().get(0))).setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream(pathPrefix + "cerchi.png"))));
            ((studentPane.getChildren().get(1))).setVisible(false);
            return;
        }
        ((ImageView) (studentPane.getChildren().get(0))).setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream(pathPrefix + "students/" + color + studentSuffix))));
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
        towerPane.setImage(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream(pathPrefix + "Pedine/tower_" + color + ".png"))));
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
     * Display all infos about the island
     * @param model reference to light model
     * @param chosenStudentID id of the moving student
     */
    public void setupIslands(LightModel model, int chosenStudentID) {
        this.chosenStudentID = chosenStudentID;
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

        islandButtons.setVisible(true);
    }
}
