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

public class Notebookpg3Controller {

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;

  // Timer utility instance
  private TimerUtility timer;

  // Method to handle the event when the middle page is clicked
  @FXML
  private void onGoMiddlePage(MouseEvent event) throws IOException {
    // Play page flip sound
    App.playSound("pageflip.mp3");
    System.out.println("Go middle page");
    // Navigate to the middle page of the notebook
    App.goToPage(event, "notebookpg2");
  }

  // Method to handle the event when the exit button is clicked
  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    // Play button click sound
    App.playSound("button.mp3");
    // Navigate to the drawers
    App.goToDrawers(event);
  }

  // Method to handle the event when the book is clicked
  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked");
    // If the book is clicked, set the bookPane to be visible and mainPane to be invisible
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  // Method to handle the event when the back button is clicked
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    // Play button click sound
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    // Navigate back to the crime scene
    App.openCrimeScene(event);
  }

  // Method to set the timer and update the timer label
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
