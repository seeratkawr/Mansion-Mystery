package nz.ac.auckland.se206.controllers;

import java.io.IOException;
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

  private static boolean initialMessageShown = false;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private ImageView loadingIndicator;
  @FXML private Label lbPopup;
  @FXML private Label lbPopup2;
  @FXML private Label timerLabel;

  private String profession; // Profession of the character
  private TranslateTransition translateTransition; // Animation for loading indicator

  /** Initializes the controller class. This method is called after the FXML fields are injected. */
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

    // Display the initial message
    if (!initialMessageShown) {
      App.initialTypedOutMessage(
          "Oh, hi detective! I’m Alex. Just, uh, doing my cleaning duties here in the mansion. It’s"
              + " a bit overwhelming with everything going on, you know?",
          txtaChat);
      initialMessageShown = true;
    } else {
      App.initialTypedOutMessage(
          "Uh, hi again. How can I help you?",
          txtaChat);
    }

    // Set the profession to Cleaner
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

  /**
   * Getter method for the timer label.
   *
   * @return The timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /**
   * Setter method for the initial message shown flag to indicate if the initial message is shown.
   *
   * @return The initial message shown flag
   */
  public void setInitialMessageShown(boolean initialMessageShown) {
    CleanerController.initialMessageShown = initialMessageShown;
  }
  
  /**
   * Method called when the map is clicked. Opens the map scene.
   *
   * @param event The mouse event that triggered the method
   */
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

  /**
   * Method called when the laptop is clicked. Opens the laptop scene.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onSendMessage(ActionEvent event) {
    App.handleGpt(profession, txtInput, txtaChat, loadingIndicator, translateTransition, null);
  }

  /**
   * Method called when the drawers are clicked. Opens the drawers scene.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.handleGuess(lbPopup, lbPopup2);
  }
}
