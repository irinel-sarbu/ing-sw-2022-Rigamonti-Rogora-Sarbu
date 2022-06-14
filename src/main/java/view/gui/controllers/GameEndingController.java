package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import network.LightModel;

public class GameEndingController implements GenericSceneController {
    @FXML
    private Label gameEndText, winnerText;
    @FXML
    private ImageView winGif0, winGif1, loseGif0;

    /**
     * Compose GameOver scene
     * @param model reference to light moder
     * @param winningPlayer name of the winning player
     */
    public void setUp(LightModel model, String winningPlayer) {
        if (model.getPlayerName().equals(winningPlayer)) {
            winGif0.setVisible(true);
            winGif1.setVisible(true);
            gameEndText.setText("You won!");
        } else {
            loseGif0.setVisible(true);
            gameEndText.setText("You lost...");
            winnerText.setText(winningPlayer + " won.");
        }
    }
}
