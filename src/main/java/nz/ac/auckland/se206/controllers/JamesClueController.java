package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// Controller for handling the James Clue scene
public class JamesClueController {
  @FXML private Rectangle rectangleButtons; // Rectangle for buttons
  @FXML private Label timerLabel; // Label to display the timer

  private TimerUtility timer; // Timer utility instance

  // Method to handle the close clue action
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    App.playSound("mouseclick.mp3"); // Play mouse click sound
    App.openLaptop(event); // Open the laptop scene
  }

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    // Add listener to update the timer label when the time changes
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

    // Create an AnimationTimer to update the timer label every frame
    AnimationTimer timerAnimation =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            timerLabel.setText(timer.formatTime(timer.getSecondsLeft()));
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
