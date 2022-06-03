package view.gui.controllers.characterControllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUseMonkEffect;
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
import util.Logger;
import util.TowerColor;
import view.gui.controllers.GenericSceneController;

import java.util.ArrayList;
import java.util.List;

public class MonkIslandSelectorSceneController implements GenericSceneController {
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

    @FXML
    public void onIslandButton0(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 0));
    }

    @FXML
    public void onIslandButton1(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 1));
    }

    @FXML
    public void onIslandButton2(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 2));
    }

    @FXML
    public void onIslandButton3(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 3));
    }

    @FXML
    public void onIslandButton4(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 4));
    }

    @FXML
    public void onIslandButton5(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 5));
    }

    @FXML
    public void onIslandButton6(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 6));
    }

    @FXML
    public void onIslandButton7(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 7));
    }

    @FXML
    public void onIslandButton8(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 8));
    }

    @FXML
    public void onIslandButton9(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 9));
    }

    @FXML
    public void onIslandButton10(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 10));
    }

    @FXML
    public void onIslandButton11(MouseEvent mouseEvent) {
        EventManager.notify(new EUseMonkEffect(chosenStudentID, 11));
    }

    private void init(LightModel model) {
        this.model = model;
        bridges = new ArrayList<>(bridges_parent.getChildren());
        islands = realm_parent.getChildren().subList(0, 12);
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
            motherNature.setVisible(i == model.getIslandGroups().get(groupByIslandID(position)).getIslands().get(0).getIslandID());
        }
    }

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
