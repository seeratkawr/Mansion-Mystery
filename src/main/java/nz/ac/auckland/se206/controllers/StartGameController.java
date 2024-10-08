package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class StartGameController {

  @FXML private Button startButton; // Button to start the game

  // Method called when the controller is initialized
  public void initialize() {
    App.playSound("startGameAudio.mp3");
  }

  @FXML
  private void onStartGame(ActionEvent event) {
    // Play a sound when the button is clicked
    App.stopSound();
    App.playSound("button.mp3");
    System.out.println("Game started!");
    try {
      App.openBackstory(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
