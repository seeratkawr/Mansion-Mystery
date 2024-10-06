package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class CrimeSceneController {

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;
  @FXML private Rectangle bookshelfSafeRectangle;
  @FXML private Rectangle drawersRectangle;
  @FXML private Button guessingButton;
  @FXML private Label lbPopup;
  @FXML private Label lbPopup2;
  @FXML private Label timerLabel;

  // Getter method for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Method to handle map click events
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      // Open the map view
      App.playSound("map.mp3"); // Play map sound
      App.openMap(event, "/images/Study.png");
      MapController.setLastScene("crimeScene"); // Set the last scene to crimeScene

    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  // Method to handle laptop click events
  @FXML
  private void onLaptopClicked(MouseEvent event) {
    App.addCluesViewed("laptop"); // Add laptop clue to the viewed clues
    try {
      App.openLaptop(event); // Open the laptop view
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to handle drawers click events
  @FXML
  private void onDrawersClicked(MouseEvent event) {
    App.addCluesViewed("drawer"); // Add drawer clue to the viewed clues
    try {
      // Open the drawer view
      App.playSound("draweropen.mp3"); // Play drawer open sound
      App.openDrawer(event); // Open the drawer view
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the bookshelf safe.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onBookshelfSafeClicked(MouseEvent event) {
    App.addCluesViewed("safe"); // Add safe clue to the viewed clues
    try {
      App.openSafe(event); // Open the safe view
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the guessing button. It will open the guess view if
   * the user has viewed at least 1 clue and spoken to all suspects.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
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

      // Display popup message for 4 seconds
      
      Timer timer = new Timer();
      App.addTimer(timer); // Store timer in App.java for garbage collection
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              lbPopup.setVisible(false); // Hide popup message after 4 seconds
              lbPopup2.setVisible(false); // Hide popup message after 4 seconds
            }
          },
          4000);
    }
  }
}
