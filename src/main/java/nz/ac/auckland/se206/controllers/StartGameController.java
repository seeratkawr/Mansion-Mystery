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

  // FXML annotation to link with the corresponding elements in the FXML file
  @FXML private Button startButton;
  @FXML private Label backstory;

  // Method called when the controller is initialized
  public void initialize() {
    // Start the fade-in effect for the backstory label
    fadeInBackstory();
  }

  // Event handler for the start button
  @FXML
  private void onStartGame(ActionEvent event) {
    // Play a sound when the button is clicked
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

  // Method to create and play a fade-in effect for the backstory label
  private void fadeInBackstory() {
    FadeTransition fadeTransition = new FadeTransition();

    // Set the duration of the fade (e.g., 3 seconds)
    fadeTransition.setDuration(Duration.seconds(3));

    // Set the label you want to fade
    fadeTransition.setNode(backstory);

    fadeTransition.setFromValue(0.0); // fully transparent
    fadeTransition.setToValue(1.0); // fully opaque

    fadeTransition.play();
  }
}
