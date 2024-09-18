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
  @FXML private Label backstory;

  public void initialize() {
    fadeInBackstory();
  }

  @FXML
  private void onStartGame(ActionEvent event) {
    App.playSound("button.mp3");
    System.out.println("Game started!");
    try {
      // Open the crime scene view
      App.openCrimeScene(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  private void fadeInBackstory() {
    // Create a new FadeTransition
    FadeTransition fadeTransition = new FadeTransition();

    // Set the duration of the fade (e.g., 2 seconds)
    fadeTransition.setDuration(Duration.seconds(3));

    // Set the label you want to fade
    fadeTransition.setNode(backstory);

    // Set the start and end opacity values
    fadeTransition.setFromValue(0.0); // fully transparent
    fadeTransition.setToValue(1.0); // fully opaque

    // Play the transition
    fadeTransition.play();
  }
}
