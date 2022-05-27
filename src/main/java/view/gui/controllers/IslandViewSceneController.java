package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.board.IslandGroup;
import model.board.IslandTile;
import network.LightModel;
import util.Color;
import util.TowerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IslandViewSceneController implements GenericSceneController {

    LightModel model;

    String pathPrefix = "/Graphical_Assets/";
    String studentSuffix = "Student.png";

    @FXML
    private AnchorPane bridges_parent;
    @FXML
    private GridPane realm_parent;

    private List<Node> bridges;
    private List<Node> islands;
    private List<Node> clouds;

    private int numOfClouds, numOfStudents;

    private void init(LightModel model) {
        this.model = model;
        numOfClouds = model.getCloudTiles().size();
        numOfStudents = numOfClouds + 1;
        bridges = new ArrayList<>(bridges_parent.getChildren());
        islands = realm_parent.getChildren().subList(0, 12);
        clouds = new ArrayList<>(realm_parent.getChildren().subList(12, 15));
    }

    private int groupByIslandID(int ID) {
        for(IslandGroup ig : model.getIslandGroups()){
            if(ig.getIslands().stream().map(IslandTile::getIslandID).toList().contains(ID))
                return ig.getIslandGroupID();
        }
        return -1;
    }

    private boolean inSameGroup(int a, int b) {
        return groupByIslandID(a) == groupByIslandID(b);
    }

    // Display functions
    private void updateBridge() {
        for(int i=0; i<12; i++) {
            if(inSameGroup(i, (i+1)%12)) bridges.get(i).setVisible(true);
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
            ((Label)(studentPane.getChildren().get(1))).setText("" + number);
        }
    }

    private void updateNoEntry(int island, int number) {
        AnchorPane noEntryPane = (AnchorPane)(((AnchorPane)islands.get(island)).getChildren().get(1));
        if (number > 0) {
            noEntryPane.setVisible(true);
        } if (number > 1) {
            ((noEntryPane.getChildren().get(1))).setVisible(true);
            ((Label)(noEntryPane.getChildren().get(1))).setText("" + number);
        }
    }

    private void updateTower(int island, TowerColor color) {
        ImageView towerPane = (ImageView) (((AnchorPane)islands.get(island)).getChildren().get(2));
        towerPane.setVisible(color != null);
        towerPane.setImage(new Image(pathPrefix + "Pedine/tower_" + color + "png"));
    }

    private void resetNoEntry() {
        for(int i=0; i<12; i++) {
            AnchorPane noEntry = ((AnchorPane)((AnchorPane)islands.get(i)).getChildren().get(1));
            noEntry.setVisible(false);
            noEntry.getChildren().get(1).setVisible(false);
        }
    }

    private void updateMotherNature(int position) {
        for(int i=0; i<12; i++) {
            ImageView motherNature = ((ImageView) ((AnchorPane) islands.get(i)).getChildren().get(3));
            motherNature.setVisible(i==position);
        }
    }

    private void resetClouds() {

        for(int i=0; i<3; i++){
            AnchorPane cloud = (AnchorPane) clouds.get(i);
            List<Node> students = new ArrayList<>(cloud.getChildren().subList(1,5));
            for(int j=0; j<4; j++) {
                (students.get(j)).setVisible(j<numOfStudents);
                ((ImageView)students.get(j)).setImage(new Image(pathPrefix + "cerchi.png"));
            }
            cloud.setVisible(i<numOfClouds);
        }
    }

    private void updateClouds(int cloudID, List<Color> studentList) {
        AnchorPane cloud = (AnchorPane) clouds.get(cloudID);
        List<Node> students = new ArrayList<>(cloud.getChildren().subList(1,5));
        for(int i=0; i<studentList.size(); i++) {
            (students.get(i)).setVisible(true);
            ((ImageView)students.get(i)).setImage(new Image(pathPrefix + "students/" + studentList.get(i) + ".png"));
        }
    }

    private void resetView() {
        resetClouds();
        resetNoEntry();
    }

    public void updateView(LightModel model) {

        init(model);
        resetView();

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
        for(IslandGroup ig : model.getIslandGroups()) {
            for(IslandTile it : ig.getIslands()) {
                updateNoEntry(it.getIslandID(), ig.getNoEntrySize());
            }
        }

        // update Towers
        for(IslandTile it : allIslands) {
            for(Color c : Color.values()) {
                updateTower(it.getIslandID(), it.getTowerColor());
            }
        }

        // update motherNature
        updateMotherNature(model.getMotherNaturePosition());

    }

}