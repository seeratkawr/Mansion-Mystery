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

public class SafeOpenedController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private AnchorPane safePane;
  @FXML private Label timerLabel;

  private TimerUtility timer;

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
