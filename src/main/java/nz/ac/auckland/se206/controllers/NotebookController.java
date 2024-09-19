package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

public class NotebookController {

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;
  @FXML private Rectangle rectangleBook;

  // Method to handle the event when the book is clicked
  @FXML
  private void onClickedBook(MouseEvent event) throws IOException {
    // Navigate to the notebook page 1
    App.goToPage(event, "notebookpg1");
  }

  // Method to handle the event when the "Go Back" button is clicked
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    // Play a sound effect for the button click
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    // Navigate back to the crime scene
    App.openCrimeScene(event);
  }

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
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
