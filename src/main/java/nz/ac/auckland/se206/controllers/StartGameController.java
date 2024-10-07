package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class StartGameController {

  @FXML private Button startButton;
  @FXML private Label backstory1;
  @FXML private Label backstory2;
  @FXML private Label backstory3;

  // Method called when the controller is initialized
  public void initialize() {
    // Set all backstory labels to invisible at first
    backstory1.setOpacity(0);
    backstory2.setOpacity(0);
    backstory3.setOpacity(0);

    App.playSound("startGameAudio.mp3");
    fadeInBackstories();
  }

  @FXML
  private void onStartGame(ActionEvent event) {
    App.playSound("button.mp3");
    System.out.println("Game started!");
    try {
      App.openCrimeScene(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to fade in the three backstory labels sequentially
  private void fadeInBackstories() {
    // Fade in backstory1
    FadeTransition fade1 = createFadeTransition(backstory1, 3);
    fade1.setOnFinished(
        event -> {
          // When backstory1 finishes, fade in backstory2
          FadeTransition fade2 = createFadeTransition(backstory2, 3);
          fade2.setOnFinished(
              event2 -> {
                // When backstory2 finishes, fade in backstory3
                FadeTransition fade3 = createFadeTransition(backstory3, 3);
                fade3.play();
              });
          fade2.play();
        });
    fade1.play();
  }

  // Helper method to create a fade transition for a given label and duration
  private FadeTransition createFadeTransition(Label label, int durationInSeconds) {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.seconds(durationInSeconds));
    fadeTransition.setNode(label);
    fadeTransition.setFromValue(0.0); // fully transparent
    fadeTransition.setToValue(1.0); // fully opaque
    return fadeTransition;
  }
}
