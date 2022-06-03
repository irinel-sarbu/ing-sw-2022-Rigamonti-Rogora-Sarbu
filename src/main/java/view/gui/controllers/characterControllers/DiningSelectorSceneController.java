package view.gui.controllers.characterControllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUseJesterEffect;
import eventSystem.events.network.client.EUseMinstrelEffect;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.board.Student;
import network.LightModel;
import util.Color;
import view.gui.controllers.EntranceSceneController;
import view.gui.controllers.GenericSceneController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiningSelectorSceneController implements GenericSceneController {

    @FXML
    private Pane GreenStudents, RedStudents, BlueStudents, PinkStudents, YellowStudents, diningPane;


    private List<Node> gStudents, rStudents, bStudents, pStudents, yStudents, tables;
    private LightModel model;
    private List<Integer> entranceStudents;
    private List<Color> selectedColors;
    private Map<Color, Integer> numOfStudents = new HashMap<>();

    @FXML
    public void table0(MouseEvent mouseEvent) {
        selectedColors.add(Color.YELLOW);
        numOfStudents.replace(Color.YELLOW, numOfStudents.get(Color.YELLOW), numOfStudents.get(Color.YELLOW) - 1);
        updateStudents();
        updateTables();
        if (selectedColors.size() >= 2) {
            EventManager.notify(new EUseMinstrelEffect(entranceStudents, selectedColors));
        }
    }

    @FXML
    public void table1(MouseEvent mouseEvent) {
        selectedColors.add(Color.BLUE);
        numOfStudents.replace(Color.BLUE, numOfStudents.get(Color.BLUE), numOfStudents.get(Color.BLUE) - 1);
        updateStudents();
        updateTables();
        if (selectedColors.size() >= 2) {
            EventManager.notify(new EUseMinstrelEffect(entranceStudents, selectedColors));
        }
    }

    @FXML
    public void table2(MouseEvent mouseEvent) {
        selectedColors.add(Color.GREEN);
        numOfStudents.replace(Color.GREEN, numOfStudents.get(Color.GREEN), numOfStudents.get(Color.GREEN) - 1);
        updateStudents();
        updateTables();
        if (selectedColors.size() >= 2) {
            EventManager.notify(new EUseMinstrelEffect(entranceStudents, selectedColors));
        }
    }

    @FXML
    public void table3(MouseEvent mouseEvent) {
        selectedColors.add(Color.RED);
        numOfStudents.replace(Color.RED, numOfStudents.get(Color.RED), numOfStudents.get(Color.RED) - 1);
        updateStudents();
        updateTables();
        if (selectedColors.size() >= 2) {
            EventManager.notify(new EUseMinstrelEffect(entranceStudents, selectedColors));
        }
    }

    @FXML
    public void table4(MouseEvent mouseEvent) {
        selectedColors.add(Color.PINK);
        numOfStudents.replace(Color.PINK, numOfStudents.get(Color.PINK), numOfStudents.get(Color.PINK) - 1);
        updateStudents();
        updateTables();
        if (selectedColors.size() >= 2) {
            EventManager.notify(new EUseMinstrelEffect(entranceStudents, selectedColors));
        }
    }

    private void initLists() {
        gStudents = GreenStudents.getChildren();
        yStudents = YellowStudents.getChildren();
        bStudents = BlueStudents.getChildren();
        rStudents = RedStudents.getChildren();
        pStudents = PinkStudents.getChildren();
        tables = diningPane.getChildren();
    }

    private void resetStudents() {
        for (int i = 0; i < 10; i++) {
            gStudents.get(i).setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            yStudents.get(i).setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            rStudents.get(i).setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            bStudents.get(i).setVisible(false);
        }
        for (int i = 0; i < 10; i++) {
            pStudents.get(i).setVisible(false);
        }
    }

    private void updateStudents() {
        resetStudents();
        for (int i = 0; i < numOfStudents.get(Color.GREEN); i++) {
            gStudents.get(i).setVisible(true);
        }
        for (int i = 0; i < numOfStudents.get(Color.RED); i++) {
            rStudents.get(i).setVisible(true);
        }
        for (int i = 0; i < numOfStudents.get(Color.YELLOW); i++) {
            yStudents.get(i).setVisible(true);
        }
        for (int i = 0; i < numOfStudents.get(Color.BLUE); i++) {
            bStudents.get(i).setVisible(true);
        }
        for (int i = 0; i < numOfStudents.get(Color.PINK); i++) {
            pStudents.get(i).setVisible(true);
        }

        if (model.getSchoolBoardMap().get(model.getPlayerName()).hasProfessor(Color.GREEN))
            gStudents.get(10).setVisible(true);
        if (model.getSchoolBoardMap().get(model.getPlayerName()).hasProfessor(Color.RED))
            rStudents.get(10).setVisible(true);
        if (model.getSchoolBoardMap().get(model.getPlayerName()).hasProfessor(Color.BLUE))
            bStudents.get(10).setVisible(true);
        if (model.getSchoolBoardMap().get(model.getPlayerName()).hasProfessor(Color.YELLOW))
            yStudents.get(10).setVisible(true);
        if (model.getSchoolBoardMap().get(model.getPlayerName()).hasProfessor(Color.PINK))
            pStudents.get(10).setVisible(true);
    }

    private void updateTables() {
        resetTables();
        for (Color color : Color.values()) {
            tables.get(color.getValue()).setVisible(false);
            if (numOfStudents.get(color) > 0) tables.get(color.getValue()).setVisible(true);
        }
    }

    private void resetTables() {
        for (Color color : Color.values()) {
            tables.get(color.getValue()).setVisible(false);
        }
    }

    public void setUpDining(LightModel model, List<Integer> entranceStudents) {
        this.model = model;
        this.entranceStudents = entranceStudents;
        this.selectedColors = new ArrayList<>();
        initLists();

        for (Color color : Color.values()) {
            numOfStudents.put(color, model.getSchoolBoardMap().get(model.getPlayerName()).getStudentsOfColor(color));
        }
        updateStudents();
        updateTables();
    }
}
