package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the Cleaner scene. This class handles the chat completion request to the
 * OpenAI API and displays the chat messages in the chat area.
 *
 * <p>It also handles the user input and sends the messages to the API for completion.
 */
public class CleanerController {
  @FXML private Button btnSend; // Button to send messages
  @FXML private TextField txtInput; // TextField for user input
  @FXML private TextArea txtaChat; // TextArea to display chat messages
  @FXML private ImageView loadingIndicator; // ImageView for loading indicator
  @FXML private Label timerLabel; // Label to display timer
  @FXML private Label lbPopup; // Label for popup message
  @FXML private Label lbPopup2;

  private static boolean initialMessageShown = false;
  private String profession; // Profession of the character
  private TranslateTransition translateTransition; // Animation for loading indicator

  // Initialize method called after the FXML fields are injected
  public void initialize() {
    loadingIndicator.setVisible(false);
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/broom.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0);
    translateTransition.setToX(285);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    txtaChat.setEditable(false);
    txtaChat.setWrapText(true);

    if (!initialMessageShown) {
      App.initialTypedOutMessage(
          "Oh, hi detective! I’m Alex. Just, uh, doing my cleaning duties here in the mansion. It’s"
              + " a bit overwhelming with everything going on, you know?",
          txtaChat);
      initialMessageShown = true;
    }

    App.setProfession("Cleaner", txtaChat, loadingIndicator, translateTransition);
    profession = App.getCurrentProfession();

    // Add event handler for the Enter key to send the message
    txtInput.setOnKeyPressed(
        event -> {
          switch (event.getCode()) {
            case ENTER:
              btnSend.fire(); // Trigger the send button programmatically
              break;
            default:
              break;
          }
        });
  }

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Event handler for map click
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      // Open the map scene and set the last scene to cleaner
      App.playSound("map.mp3"); // Play map sound
      App.openMap(event, "/images/cleaner.png");
      MapController.setLastScene("cleaner");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Event handler for send button click
  @FXML
  private void onSendMessage(ActionEvent event) {
    App.handleGpt(profession, txtInput, txtaChat, loadingIndicator, translateTransition, null);
  }

  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.handleGuess(lbPopup, lbPopup2);
  }
}
