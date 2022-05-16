package view.gui.controllers;

import com.sun.javafx.scene.control.IntegerField;
import eventSystem.EventManager;
import eventSystem.events.local.EUpdateServerInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.Logger;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {

    public TextField ip;
    public TextField port;
    public Label warning;

    private File menuSong;
    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        menuSong = new File("src/main/resources/bgMusic/ServerConnectionMusic.mp3");
        media = new Media(menuSong.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        Logger.info("Login button Pressed");
        String portValue = port.getText();
        if (portValue.matches("\\d{1,8}")) {
            EventManager.notify(new EUpdateServerInfo(ip.getText(), Integer.parseInt(portValue)));
            warning.opacityProperty().setValue(0);
        } else {
            warning.opacityProperty().setValue(100);
        }
    }
}
