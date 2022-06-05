package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.network.client.EWizardChosen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import util.Wizard;

import java.util.ArrayList;
import java.util.List;


public class WizardChoiceController implements GenericSceneController {
    @FXML
    private Label wizardLabel, wizardChosenText, menuLabel;
    @FXML
    private Pane wizards;
    @FXML
    private ImageView wizard0, wizard1, wizard2, wizard3;

    /**
     * notify selection of the wizard 0
     * @param mouseEvent
     */
    @FXML
    public void onWizard0(MouseEvent mouseEvent) {
        wizards.setVisible(false);
        wizardChosenText.setVisible(true);
        EventManager.notify(new EWizardChosen(Wizard.WIZARD_1));
    }

    /**
     * notify selection of the wizard 1
     * @param mouseEvent
     */
    @FXML
    public void onWizard1(MouseEvent mouseEvent) {
        wizards.setVisible(false);
        wizardChosenText.setVisible(true);
        EventManager.notify(new EWizardChosen(Wizard.WIZARD_2));
    }

    /**
     * notify selection of the wizard 2
     * @param mouseEvent
     */
    @FXML
    public void onWizard2(MouseEvent mouseEvent) {
        wizards.setVisible(false);
        wizardChosenText.setVisible(true);
        EventManager.notify(new EWizardChosen(Wizard.WIZARD_3));
    }

    /**
     * notify selection of the wizard 3
     * @param mouseEvent
     */
    @FXML
    public void onWizard3(MouseEvent mouseEvent) {
        wizards.setVisible(false);
        wizardChosenText.setVisible(true);
        EventManager.notify(new EWizardChosen(Wizard.WIZARD_4));
    }

    /**
     * display wizard if they are available
     * @param availableWizards list of available wizards
     */
    public void chooseWizards(List<Wizard> availableWizards) {
        wizardLabel.setVisible(false);
        wizards.setVisible(true);
        for (Wizard wizard : availableWizards) {
            switch (wizard.getValue()) {
                case 1 -> wizard0.setVisible(true);
                case 2 -> wizard1.setVisible(true);
                case 3 -> wizard2.setVisible(true);
                case 4 -> wizard3.setVisible(true);
            }
        }
    }
}
