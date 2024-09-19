package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// Controller class for handling the Maria Clue scene
public class MariaClueController {
  // FXML injected Rectangle element for buttons
  @FXML private Rectangle rectangleButtons;

  // FXML injected Label element for displaying the timer
  @FXML private Label timerLabel;

  // Timer utility instance
  private TimerUtility timer;

  // Method to handle the closing of the clue window
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Open the laptop scene
    App.openLaptop(event);
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
  }

  // Method to set the timer and initialize the timer label updates
  public void setTimer(TimerUtility timer) {
    this.timer = timer;

    // Add a listener to update the timer label whenever the time changes
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

    // Create an AnimationTimer to continuously update the timer label
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

  // Getter method for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }
}
