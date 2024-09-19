package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

/**
 * This class is the controller for the Safe.fxml file. It contains the logic for the safe scene.
 */
public class SafeController {

  // FXML injected fields
  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private AnchorPane safePane;
  @FXML private Label timerLabel;
  @FXML private Rectangle rectangleKeypad;

  /**
   * Sets the timer and initializes the timer label to update with the timer's value.
   *
   * @param timer the TimerUtility instance to be used
   */
  public void setTimer(TimerUtility timer) {

    // Add a listener to update the timer label when the timer's value changes
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

  /**
   * Gets the timer label.
   *
   * @return the timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /** This method initializes the controller. */
  @FXML
  void initialize() {
    // Ensure that the safePane is injected properly
    assert safePane != null
        : "fx:id=\"crimeScenePane\" was not injected: check your FXML file 'Safe.fxml'.";
  }

  /**
   * This method is called when the user clicks the fingerprint.
   *
   * @param event the event that triggered this method
   */
  @FXML
  void onClickedFingerprint(MouseEvent event) {
    System.out.println("Fingerprint clicked");
  }

  /**
   * This method is called when the user clicks the keypad.
   *
   * @param event the event that triggered this method
   * @throws IOException if an I/O error occurs
   */
  @FXML
  void onClickedKeypad(MouseEvent event) throws IOException {
    System.out.println("Keypad clicked");
    App.openSafeKeypad(event);
  }

  /**
   * This method is called when the user clicks the back button. It returns users to the crime
   * scene.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onGoBackCrimeScene(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    App.openCrimeScene(event);
  }
}
