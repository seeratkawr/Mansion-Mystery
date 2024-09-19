/**
 * NoteBookpg1Controller is responsible for handling the interactions on the first page of the
 * notebook. It manages the timer display and navigation between pages.
 *
 * <p>This controller uses FXML annotations to bind UI components and event handlers. It also
 * integrates with the TimerUtility class to update the timer label in real-time.
 *
 * <p>Methods:
 *
 * <ul>
 *   <li>{@link #onGoMiddlePage(MouseEvent)} - Handles the event when the user navigates to the
 *       middle page of the notebook.
 *   <li>{@link #onExitBook(ActionEvent)} - Handles the event when the user exits the notebook.
 *   <li>{@link #setTimer(TimerUtility)} - Sets the timer and initializes the timer label updates.
 *   <li>{@link #getTimerLabel()} - Returns the timer label.
 * </ul>
 *
 * <p>Dependencies:
 *
 * <ul>
 *   <li>{@link App} - For navigation and sound playing functionalities.
 *   <li>{@link TimerUtility} - For managing the timer logic.
 * </ul>
 *
 * <p>FXML Components:
 *
 * <ul>
 *   <li>{@link #timerLabel} - Label to display the timer countdown.
 * </ul>
 *
 * <p>Event Handlers:
 *
 * <ul>
 *   <li>{@link #onGoMiddlePage(MouseEvent)} - Triggered when the user clicks to go to the middle
 *       page.
 *   <li>{@link #onExitBook(ActionEvent)} - Triggered when the user clicks to exit the notebook.
 * </ul>
 *
 * <p>Initialization:
 *
 * <ul>
 *   <li>{@link #setTimer(TimerUtility)} - Initializes the timer and starts the AnimationTimer for
 *       real-time updates.
 * </ul>
 *
 * <p>Author: [Your Name]
 *
 * <p>Date: [Date]
 */
package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

public class NoteBookpg1Controller {

  @FXML private Label timerLabel;

  private TimerUtility timer;

  @FXML
  private void onGoMiddlePage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3");
    App.goToPage(event, "notebookpg2");
    System.out.println("Go right page");
  }

  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back");
    // if the back button is clicked, set the bookpane to be invisible
    App.goToDrawers(event);
  }

  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

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
    return timerLabel;
  }
}
