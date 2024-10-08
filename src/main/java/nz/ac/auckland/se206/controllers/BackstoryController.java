package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class BackstoryController {
  @FXML private Label backstory1;
  @FXML private Label backstory2;
  @FXML private Label backstory3;
  @FXML private Button openCrimeScene;
  @FXML private Label timerLabel;

  @FXML
  private void onOpenCrimeScene(ActionEvent event) throws IOException {
    App.openCrimeScene(event);
  }

  @FXML
  private void initialize() {
    // Initially hide the backstory labels
    backstory1.setVisible(false);
    backstory2.setVisible(false);
    backstory3.setVisible(false);

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

    Timer timer = new Timer();
    App.addTimer(timer);
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            try {
              App.openCrimeScene(null);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        },
        15000);
  }

  private FadeTransition createFadeTransition(Label label, Duration duration, Duration delay) {
    FadeTransition fade = new FadeTransition(duration, label);
    fade.setFromValue(0.0);
    fade.setToValue(1.0);
    fade.setDelay(delay);
    fade.setOnFinished(e -> label.setVisible(true)); // Ensure label is visible after fade-in
    return fade;
  }
}
