package view.gui.controllers;


import eventSystem.EventManager;
import eventSystem.events.local.EUpdateNickname;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.Logger;


import java.io.File;
import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ResourceBundle;


public class NameSelectionSceneController implements Initializable {
    public TextField name;

    private File menuSong;
    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        menuSong = new File("src/main/resources/bgMusic/MainMenuMusic.mp3");
        media = new Media(menuSong.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @FXML
    public void onNameConfirmed(MouseEvent mouseEvent) {
        Logger.info("Name Selected: " + name.getText());
        EventManager.notify(new EUpdateNickname(name.getText()));
    }
}
