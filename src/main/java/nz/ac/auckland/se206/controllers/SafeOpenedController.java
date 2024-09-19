package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

/**
 * The SafeOpenedController class is responsible for handling the interactions and logic for the
 * "safe opened" scene in the application. It manages the timer display and handles user actions
 * such as going back to the previous scene.
 *
 * <p>This controller uses JavaFX annotations to link UI components defined in the FXML file
 * 'safeOpened.fxml' and provides methods to initialize the controller and handle user events.
 *
 * <p>Fields:
 *
 * <ul>
 *   <li>{@code resources} - The resources used to localize the UI components.
 *   <li>{@code location} - The location of the FXML file that defines the UI components.
 *   <li>{@code safePane} - The main container for the "safe opened" scene.
 *   <li>{@code timerLabel} - The label that displays the remaining time.
 *   <li>{@code timer} - The utility class that manages the timer logic.
 * </ul>
 *
 * <p>Methods:
 *
 * <ul>
 *   <li>{@link #setTimer(TimerUtility)} - Sets the timer utility and initializes the timer display.
 *   <li>{@link #getTimerLabel()} - Returns the label that displays the remaining time.
 *   <li>{@link #initialize()} - Initializes the controller and ensures the FXML components are
 *       injected.
 *   <li>{@link #onGoBackSafe(MouseEvent)} - Handles the event when the user clicks the go back
 *       button, returning the user to the previous scene.
 * </ul>
 *
 * <p>Exceptions:
 *
 * <ul>
 *   <li>{@link IOException} - Thrown by {@link #onGoBackSafe(MouseEvent)} if the FXML file is not
 *       found.
 * </ul>
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link FXML} - Indicates that a field or method is linked to an FXML component or event.
 * </ul>
 *
 * @see TimerUtility
 * @see App
 */
public class SafeOpenedController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private AnchorPane safePane;
  @FXML private Label timerLabel;

  public void setTimer(TimerUtility timer) {
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

  /** This method initializes the controller. */
  @FXML
  void initialize() {
    assert safePane != null
        : "fx:id=\"safePane\" was not injected: check your FXML file 'safeOpened.fxml'.";
  }

  /**
   * This method is called when the user clicks the go back button. It returns users to the safe
   * scene.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onGoBackSafe(MouseEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to safe closed");
    App.openSafe(event);
  }
}
