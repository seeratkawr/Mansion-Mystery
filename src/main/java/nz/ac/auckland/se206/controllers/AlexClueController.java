package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// Controller class for handling the Alex Clue scene
public class AlexClueController {
  @FXML private Rectangle rectangleButtons; // Rectangle for button visuals
  @FXML private Label timerLabel; // Label to display the timer

  private TimerUtility timer; // Utility class for managing the timer

  // Method to handle the closing of the clue window
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    App.playSound("mouseclick.mp3"); // Play a sound on mouse click
    App.openLaptop(event); // Open the laptop scene
  }

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    // Add a listener to update the timer label whenever the time changes
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
