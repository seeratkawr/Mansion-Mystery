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
 * The KitchenController class is responsible for handling the interactions within the kitchen scene
 * of the application. It manages the chat interface, user input, and animations related to the
 * kitchen scene.
 */
public class KitchenController {

  @FXML private TextArea txtaChat; // Text area for chat messages
  @FXML private TextField txtInput; // Text field for user input
  @FXML private Button btnSend; // Button to send messages
  @FXML private ImageView loadingIndicator; // Loading indicator image
  @FXML private Label timerLabel; // Label to display the timer
  @FXML private ImageView mapImage; // Image view for the map
  @FXML private Label lbPopup; // Label for popup message
  @FXML private Label lbPopup2;

  private String profession; // Profession of the character
  private TranslateTransition translateTransition; // Animation for loading indicator
  private static boolean initialMessageShown = false;

  // Initialize method called after FXML fields are populated
  public void initialize() {
    loadingIndicator.setVisible(false); // Hide loading indicator initially
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/spatula.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0); // Start position (off-screen)
    translateTransition.setToX(310); // End position (adjust as needed)
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE); // Loop the animation
    translateTransition.setAutoReverse(true); // Move back and forth
    txtaChat.setWrapText(true); // Enable text wrapping in chat area
    txtaChat.setEditable(false);

    if (!initialMessageShown) {
      App.initialTypedOutMessage(
          "Hi detective! I'm John, the chef here. Nice to meet you! What brings you here?",
          txtaChat);
      initialMessageShown = true;
    }

    App.setProfession("Chef", txtaChat, loadingIndicator, translateTransition);
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

  // Get the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Handle map click event
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.playSound("map.mp3");
      App.openMap(event, "/images/Kitchen.png");
      MapController.setLastScene("kitchen");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Map clicked");
  }

  // Handle send message button click event
  @FXML
  private void onSendMessage(ActionEvent event) throws IOException {
    App.handleGpt(profession, txtInput, txtaChat, loadingIndicator, translateTransition, null);
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
