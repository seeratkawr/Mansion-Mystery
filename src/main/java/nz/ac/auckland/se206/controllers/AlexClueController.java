package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// Controller class for handling the Alex Clue screen
public class AlexClueController {

  // FXML injected Label for displaying the timer
  @FXML private Label timerLabel;
  // FXML injected Circle elements for the buttons
  @FXML private Circle circleButton1;
  @FXML private Circle circleButton2;
  @FXML private Circle circleButton3;

  // Method to handle the close clue action
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
    // Open the laptop screen
    App.openLaptop(event);
  }

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    // Add a listener to update the timer label when the time changes
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(
                  timer.formatTime(newTime.intValue())); // Update the label with formatted time
            });

    // Create an AnimationTimer to continuously update the timer label
    AnimationTimer timerAnimation =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            timerLabel.setText(
                timer.formatTime(
                    timer.getSecondsLeft())); // Update the label with the remaining time
          }
        };

    // Start the AnimationTimer
    timerAnimation.start();
  }

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }
}
