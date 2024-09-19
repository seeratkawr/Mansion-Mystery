package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

public class Notebookpg2Controller {

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;

  // Timer utility instance
  private TimerUtility timer;

  // Method to handle the event when the user clicks to go to the last page
  @FXML
  private void onGoLastPage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3"); // Play page flip sound
    System.out.println("Go last page"); // Log action
    App.goToPage(event, "notebookpg3"); // Navigate to the last page
  }

  // Method to handle the event when the book is clicked
  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked"); // Log action
    // Make the bookPane visible and hide the mainPane
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  // Method to handle the event when the exit book button is clicked
  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.playSound("button.mp3"); // Play button click sound
    App.goToDrawers(event); // Navigate to the drawers
  }

  // Method to handle the event when the user clicks to go back to the crime scene
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    App.playSound("button.mp3"); // Play button click sound
    System.out.println("Go back to crime scene"); // Log action
    App.openCrimeScene(event); // Navigate to the crime scene
  }

  // Method to handle the event when the user clicks to go to the first page
  @FXML
  private void onGoFirstPage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3"); // Play page flip sound
    System.out.println("Go first page"); // Log action
    App.goToPage(event, "notebookpg1"); // Navigate to the first page
  }

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    // Add a listener to update the timer label when the time changes
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

    // Create an AnimationTimer to update the timer label in real-time
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
