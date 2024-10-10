package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/** Controller class for handling the backstory scene. */
public class BackstoryController {
  @FXML private Label backstory1;
  @FXML private Label backstory2;
  @FXML private Label backstory3;
  @FXML private Button openCrimeScene;
  @FXML private Label timerLabel;

  private boolean crimeSceneOpened = false;

  /**
   * Getter for the timer label.
   *
   * @return The timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /**
   * Method called when the open crime scene button is clicked. Opens the crime scene.
   *
   * @param event The action event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onOpenCrimeScene(ActionEvent event) throws IOException {
    App.openCrimeScene();
    crimeSceneOpened = true;
  }

  /** Initializes the controller class. This method is called after the FXML fields are injected. */
  @FXML
  private void initialize() {
    // Initially hide the backstory labels
    backstory1.setOpacity(0.0);
    backstory2.setOpacity(0.0);
    backstory3.setOpacity(0.0);

    // Set up fade transitions for each backstory label
    FadeTransition fade1 =
        createFadeTransition(backstory1, Duration.seconds(1), Duration.seconds(0));
    FadeTransition fade2 =
        createFadeTransition(backstory2, Duration.seconds(1), Duration.seconds(2));
    FadeTransition fade3 =
        createFadeTransition(backstory3, Duration.seconds(1), Duration.seconds(4));

    // Play the fade transitions in sequence
    fade1.play();
    fade2.play();
    fade3.play();

    // Timer to automatically open the crime scene if not opened manually
    Timer timer = new Timer();
    App.addTimer(timer);
    timer.schedule(
        // Timer task to run after 15 seconds
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(
                () -> {
                  try {
                    // Open the crime scene if it has not been opened
                    if (!crimeSceneOpened) {
                      App.openCrimeScene();
                      timer.cancel();
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                });
          }
        },
        15000);
  }

  /**
   * Creates a fade transition for a label with the specified duration and delay.
   *
   * @param label The label to apply the fade transition to
   * @param duration The duration of the fade transition
   * @param delay The delay before the fade transition starts
   * @return The fade transition
   */
  private FadeTransition createFadeTransition(Label label, Duration duration, Duration delay) {
    FadeTransition fade = new FadeTransition(duration, label);
    fade.setFromValue(0.0); // Start from fully transparent
    fade.setToValue(1.0); // End at fully visible
    fade.setDelay(delay); // Add delay to control when the fade starts
    return fade;
  }
}
