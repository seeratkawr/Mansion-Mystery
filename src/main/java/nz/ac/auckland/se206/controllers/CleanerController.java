package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CleanerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea cleanerText;

    @FXML
    private ImageView loadingIndicator;

    @FXML
    private ImageView mapImage;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textInput;

    @FXML
    void onMapClicked(MouseEvent event) {

    }

    @FXML
    void onSendMessage(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert cleanerText != null : "fx:id=\"cleanerText\" was not injected: check your FXML file 'cleaner.fxml'.";
        assert loadingIndicator != null
                : "fx:id=\"loadingIndicator\" was not injected: check your FXML file 'cleaner.fxml'.";
        assert mapImage != null : "fx:id=\"mapImage\" was not injected: check your FXML file 'cleaner.fxml'.";
        assert sendButton != null : "fx:id=\"sendButton\" was not injected: check your FXML file 'cleaner.fxml'.";
        assert textInput != null : "fx:id=\"textInput\" was not injected: check your FXML file 'cleaner.fxml'.";

    }

}
