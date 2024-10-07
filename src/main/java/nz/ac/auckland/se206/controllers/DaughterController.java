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

// Controller class for the Daughter scene
public class DaughterController {
  @FXML private Button btnSend; // Button to send the message
  @FXML private TextField txtInput; // Text field for user input
  @FXML private TextArea daughterText; // Text area to display chat messages
  @FXML private ImageView loadingIndicator; // Loading indicator image
  @FXML private Label timerLabel; // Label to display the timer
  @FXML private Label lbPopup; // Label for popup message
  @FXML private Label lbPopup2;

  private String profession; // Profession of the character
  private TranslateTransition translateTransition; // Animation for loading indicator

  // Initialize method called after the FXML fields are populated
  public void initialize() {
    loadingIndicator.setVisible(false); // Hide loading indicator initially
    loadingIndicator.setImage(
        new Image(
            getClass().getResourceAsStream("/images/bear.png"))); // Set loading indicator image
    translateTransition =
        new TranslateTransition(
            Duration.seconds(2), loadingIndicator); // Create translate transition animation
    translateTransition.setFromX(0);
    translateTransition.setToX(300);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    daughterText.setEditable(false); // Make text area non-editable
    daughterText.setWrapText(true); // Enable text wrapping
    daughterText.setText(
        "Hi detective. I assume you should already know who I am, but just in case, I'm Maria, the"
            + " daughter of the owner of this mansion. What do you want anyway?");

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

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Event handler for map click
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

  // Event handler for send button click
  @FXML
  private void onSendMessage(ActionEvent event) {
    App.handleGpt(profession, txtInput, daughterText, loadingIndicator, translateTransition, null);
  }

  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.playSound("button.mp3"); // Play button click sound
    System.out.println("Guessing button clicked");

    // verifyCanGuess() returns a list of booleans in the format
    // [enoughSuspectsTalkedTo, enoughCluesViewed, canGuess]
    List<Boolean> canGuessList = App.verifyCanGuess();
    Boolean canGuess = canGuessList.get(2);
    Boolean enoughCluesViewed = canGuessList.get(1);
    Boolean enoughSuspectsTalkedTo = canGuessList.get(0);

    // Verify if the user can guess
    if (canGuess) {
      App.openGuessingScene(); // Open the guessing scene
    } else {
      // Update the popup message based on the user's progress
      if (!enoughSuspectsTalkedTo && !enoughCluesViewed) {
        lbPopup2.setVisible(true);
      } else if (!enoughSuspectsTalkedTo) {
        lbPopup.setText("You need to talk to all suspects before making a guess.");
        lbPopup.setVisible(true);
      } else if (!enoughCluesViewed) {
        lbPopup.setText("You need to view at least 1 clue before making a guess.");
        lbPopup.setVisible(true);
      }
    }
  }
}
