package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EUseCharacterEffect;
import eventSystem.events.network.client.actionPhaseRelated.ESelectRefillCloud;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.expert.CharacterCard;
import network.LightModel;
import util.CharacterType;
import view.gui.SceneController;
import view.gui.controllers.characterControllers.HeraldIslandSelectorSceneController;
import view.gui.controllers.characterControllers.MonkSelectionSceneController;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionSceneController implements GenericSceneController {
    @FXML
    private Pane extractedCharacters, activeEffectPane;
    @FXML
    private ImageView character_0, noEntry_0_0, noEntry_0_1, noEntry_0_2, noEntry_0_3, student_0_0, student_0_1, student_0_2, student_0_3, student_0_4, student_0_5;
    @FXML
    private ImageView character_1, noEntry_1_0, noEntry_1_1, noEntry_1_2, noEntry_1_3, student_1_0, student_1_1, student_1_2, student_1_3, student_1_4, student_1_5;
    @FXML
    private ImageView character_2, noEntry_2_0, noEntry_2_1, noEntry_2_2, noEntry_2_3, student_2_0, student_2_1, student_2_2, student_2_3, student_2_4, student_2_5;
    @FXML
    private ImageView character_A, noEntry_A_0, noEntry_A_1, noEntry_A_2, noEntry_A_3, student_A_0, student_A_1, student_A_2, student_A_3, student_A_4, student_A_5;
    @FXML
    private Label coins0, coins1, coins2, coinsA;

    private List<Node> noEntry_0, noEntry_1, noEntry_2, noEntry_A, student_0, student_1, student_2, student_A;
    private LightModel model;

    @FXML
    public void onBack() {
        SceneController.switchScene("genericMenuScene.fxml");
        GenericMenuSceneController controller = (GenericMenuSceneController) SceneController.getCurrentSceneController();
        controller.setController(model, model.getPlayerName());

    }

    @FXML
    private void onChar0(MouseEvent mouseEvent) {
        activateEffect(0);
    }

    @FXML
    private void onChar1(MouseEvent mouseEvent) {
        activateEffect(1);
    }

    @FXML
    private void onChar2(MouseEvent mouseEvent) {
        activateEffect(2);
    }

    private void activateEffect(int charId) {
        switch (model.getCharacters().get(charId).getCharacter()) {
            case POSTMAN -> {
                EventManager.notify(new EUseCharacterEffect(CharacterType.POSTMAN));
            }
            case CENTAUR -> {
                EventManager.notify(new EUseCharacterEffect(CharacterType.CENTAUR));
            }
            case KNIGHT -> {
                EventManager.notify(new EUseCharacterEffect(CharacterType.KNIGHT));
            }
            case FARMER -> {
                EventManager.notify(new EUseCharacterEffect(CharacterType.FARMER));
            }
            case MONK -> {
                SceneController.switchScene("monkSelection.fxml");
                MonkSelectionSceneController controller = (MonkSelectionSceneController) SceneController.getCurrentSceneController();
                controller.setUpCharacterChoice(model);
            }
            case HERALD -> {
                SceneController.switchScene("heraldIslandSelection.fxml");
                HeraldIslandSelectorSceneController controller = (HeraldIslandSelectorSceneController) SceneController.getCurrentSceneController();
                controller.setupIslands(model);
            }
        }
    }

    private void setCharactersUp() {
        noEntry_0 = new ArrayList<>();
        noEntry_0.add(noEntry_0_0);
        noEntry_0.add(noEntry_0_1);
        noEntry_0.add(noEntry_0_2);
        noEntry_0.add(noEntry_0_3);
        noEntry_1 = new ArrayList<>();
        noEntry_1.add(noEntry_1_0);
        noEntry_1.add(noEntry_1_1);
        noEntry_1.add(noEntry_1_2);
        noEntry_1.add(noEntry_1_3);
        noEntry_2 = new ArrayList<>();
        noEntry_2.add(noEntry_2_0);
        noEntry_2.add(noEntry_2_1);
        noEntry_2.add(noEntry_2_2);
        noEntry_2.add(noEntry_2_3);
        noEntry_A = new ArrayList<>();
        noEntry_A.add(noEntry_A_0);
        noEntry_A.add(noEntry_A_1);
        noEntry_A.add(noEntry_A_2);
        noEntry_A.add(noEntry_A_3);
        student_0 = new ArrayList<>();
        student_0.add(student_0_0);
        student_0.add(student_0_1);
        student_0.add(student_0_2);
        student_0.add(student_0_3);
        student_0.add(student_0_4);
        student_0.add(student_0_5);
        student_1 = new ArrayList<>();
        student_1.add(student_1_0);
        student_1.add(student_1_1);
        student_1.add(student_1_2);
        student_1.add(student_1_3);
        student_1.add(student_1_4);
        student_1.add(student_1_5);
        student_2 = new ArrayList<>();
        student_2.add(student_2_0);
        student_2.add(student_2_1);
        student_2.add(student_2_2);
        student_2.add(student_2_3);
        student_2.add(student_2_4);
        student_2.add(student_2_5);
        student_A = new ArrayList<>();
        student_A.add(student_A_0);
        student_A.add(student_A_1);
        student_A.add(student_A_2);
        student_A.add(student_A_3);
        student_A.add(student_A_4);
        student_A.add(student_A_5);
    }

    //IMPORTANT: DO NOT change character cards name
    public void setUpCharacterChoice(LightModel model) {
        this.model = model;
        setCharactersUp();
        if (model.getActiveCharacterEffect() == null) {
            CharacterCard character = model.getCharacters().get(0);
            loadCharacterPieces(character, coins0, character_0, noEntry_0, student_0);
            character = model.getCharacters().get(1);
            loadCharacterPieces(character, coins1, character_1, noEntry_1, student_1);
            character = model.getCharacters().get(2);
            loadCharacterPieces(character, coins2, character_2, noEntry_2, student_2);
            extractedCharacters.setVisible(true);
        } else {
            for (CharacterCard character : model.getCharacters()) {
                if (character.getCharacter() == model.getActiveCharacterEffect()) {
                    loadCharacterPieces(character, coinsA, character_A, noEntry_A, student_A);
                }
            }
            activeEffectPane.setVisible(true);
        }
    }

    private void loadCharacterPieces(CharacterCard character, Label coinsTmp, ImageView character_Tmp, List<Node> noEntry_Tmp, List<Node> student_Tmp) {
        coinsTmp.setText(String.valueOf(character.getCost()));
        character_Tmp.setImage(new Image("/Graphical_Assets/Personaggi/Character_" + character.getCharacter().getNumber() + ".jpg"));
        if (character.getNoEntryTiles() != null) {
            for (int i = 0; i < character.getNoEntryTiles().size(); i++) {
                noEntry_Tmp.get(i).setVisible(true);
            }
        }
        if (character.getStudents() != null) {
            for (int i = 0; i < character.getStudents().size(); i++) {
                ImageView student = (ImageView) student_Tmp.get(i);
                student.setImage(new Image("/Graphical_Assets/students/" + character.getStudents().get(i).getColor().toString() + "StudentResized.png"));
                student.setVisible(true);
            }
        }
    }
}
