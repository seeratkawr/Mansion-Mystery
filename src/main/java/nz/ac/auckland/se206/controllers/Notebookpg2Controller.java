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

  @FXML private Pane bookPane; // Pane for the book
  @FXML private Pane mainPane; // Main pane of the scene
  @FXML private Label timerLabel; // Label to display the timer

  private TimerUtility timer; // Timer utility instance

  @FXML
  private void onGoLastPage(MouseEvent event) throws IOException {
    // Play page flip sound
    App.playSound("pageflip.mp3");
    System.out.println("Go last page");
    // Navigate to the last page of the notebook
    App.goToPage(event, "notebookpg3");
  }

  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked");
    // If the book is clicked, set the bookPane to be visible and mainPane to be invisible
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    // Play button click sound
    App.playSound("button.mp3");
    // Navigate to the drawers scene
    App.goToDrawers(event);
  }

  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    // Play button click sound
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    // Navigate back to the crime scene
    App.openCrimeScene(event);
  }

  @FXML
  private void onGoFirstPage(MouseEvent event) throws IOException {
    // Play page flip sound
    App.playSound("pageflip.mp3");
    System.out.println("Go first page");
    // Navigate to the first page of the notebook
    App.goToPage(event, "notebookpg1");
  }

  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    // Add a listener to update the timer label whenever the time changes
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

  public Label getTimerLabel() {
    return timerLabel; // Getter for the timer label
  }
}
