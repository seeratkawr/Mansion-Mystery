package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// Controller class for handling the James Clue scene
public class JamesClueController {
  // FXML injected Rectangle for button area
  @FXML private Rectangle rectangleButtons;
  // FXML injected Label for displaying the timer
  @FXML private Label timerLabel;

  // Timer utility instance
  private TimerUtility timer;

  // Method to handle the close clue action
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
    // Open the laptop scene
    App.openLaptop(event);
  }

  // Method to set the timer utility and initialize the timer label
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
