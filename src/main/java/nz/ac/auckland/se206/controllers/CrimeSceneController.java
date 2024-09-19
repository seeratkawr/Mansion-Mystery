package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Import IOException for handling file-related exceptions
import java.util.Timer; // Import Timer for scheduling tasks
import javafx.animation.AnimationTimer; // Import AnimationTimer for creating a custom timer
import javafx.event.ActionEvent; // Import ActionEvent for handling button click events
import javafx.fxml.FXML; // Import FXML for JavaFX annotations
import javafx.scene.control.Button; // Import Button for JavaFX button control
import javafx.scene.control.Label; // Import Label for JavaFX label control
import javafx.scene.image.ImageView; // Import ImageView for displaying images
import javafx.scene.input.MouseEvent; // Import MouseEvent for handling mouse events
import javafx.scene.shape.Rectangle; // Import Rectangle for JavaFX rectangle shape
import nz.ac.auckland.se206.App; // Import App for accessing application-wide methods
import nz.ac.auckland.se206.TimerUtility; // Import TimerUtility for custom timer utility

public class CrimeSceneController {

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;
  @FXML private Rectangle bookshelfSafeRectangle;
  @FXML private Button guessingButton;
  @FXML private Label lbPopup;
  @FXML private Label timerLabel;

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue())); // Update timer label
            });

    // Create an AnimationTimer to continuously update the timer label
    AnimationTimer timerAnimation =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            timerLabel.setText(timer.formatTime(timer.getSecondsLeft())); // Update timer label
          }
        };

    // Start the AnimationTimer
    timerAnimation.start();
  }

  // Getter method for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Method to handle map click events
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      // Open the map view
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
    Boolean canGuess = App.verifyCanGuess().get(2);
    Boolean enoughCluesViewed = App.verifyCanGuess().get(1);
    Boolean enoughSuspectsTalkedTo = App.verifyCanGuess().get(0);

    // Verify if the user can guess
    if (canGuess) {
      App.openGuessingScene(); // Open the guessing scene
    } else {
      // Update the popup message based on the user's progress
      if (!enoughSuspectsTalkedTo) {
        lbPopup.setText("You need to talk to all suspects before making a guess.");
      } else if (!enoughCluesViewed) {
        lbPopup.setText("You need to view at least 1 clue before making a guess.");
      }

      // Display popup message for 3 seconds
      lbPopup.setVisible(true);
      Timer timer = new java.util.Timer();
      App.addTimer(timer); // Store timer in App.java for garbage collection
      timer.schedule(
          new java.util.TimerTask() {
            @Override
            public void run() {
              lbPopup.setVisible(false); // Hide popup message after 3 seconds
            }
          },
          3000);
    }
  }
}
