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
 * The DaughterController class is responsible for handling the user interactions and game logic for
 * the daughter character. It manages the UI components, handles user input, and communicates with
 * the AI to process the user's messages.
 */
public class DaughterController {
  private static boolean initialMessageShown =
      false; // Flag to check if the initial message is shown

  @FXML private TextArea daughterText;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private ImageView loadingIndicator;
  @FXML private Label lbPopup;
  @FXML private Label lbPopup2;
  @FXML private Label timerLabel;

  private String profession;
  private TranslateTransition translateTransition;

  /** Initializes the controller class. This method is called after the FXML fields are injected. */
  public void initialize() {
    loadingIndicator.setVisible(false);
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/bear.png")));
    translateTransition =
        new TranslateTransition(
            Duration.seconds(2), loadingIndicator); // Create translate transition animation
    translateTransition.setFromX(0);
    translateTransition.setToX(300);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    daughterText.setEditable(false); // Make text area non-editable
    daughterText.setWrapText(true); // Enable text wrapping

    // Display the initial message
    if (!initialMessageShown) {
      App.initialTypedOutMessage(
          "Hi detective. I assume you should already know who I am, but just in case, "
              + "I'm Maria, the daughter of the owner of this mansion. What do you want anyway?",
          daughterText);
      initialMessageShown = true;
    }

    App.setProfession("Daughter", daughterText, loadingIndicator, translateTransition);
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
   * Method to get the chat area of the daughter scene.
   *
   * @return The chat area of the daughter scene
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
    DaughterController.initialMessageShown = initialMessageShown;
  }

  /**
   * Method to handle the event when the user clicks on the map. It plays the map sound and opens
   * the map.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.playSound("map.mp3"); // Play map sound
      App.openMap(event, "/images/bedroom.jpg"); // Open the map with the specified image
      MapController.setLastScene("daughter"); // Set the last scene to daughter
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to handle the event when the user clicks on the send button. It sends the message to the
   * AI.
   *
   * @param event The action event that triggered the method
   */
  @FXML
  private void onSendMessage(ActionEvent event) {
    App.handleGpt(profession, txtInput, daughterText, loadingIndicator, translateTransition, null);
  }

  /**
   * Method to handle the event when the user clicks on the chef rectangle. It sets the chosen
   * suspect.
   *
   * @param event The mouse event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.handleGuess(lbPopup, lbPopup2);
  }
}
