package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class StartGameController {

  @FXML private Button startButton; // Button to start the game

  /** Initializes the controller class. This method is called after the FXML fields are injected. */
  public void initialize() {
    App.playSound("startGameAudio.mp3");
  }

  /**
   * Method called when the start button is clicked. Opens the backstory scene.
   *
   * @param event The action event that triggered the method
   */
  @FXML
  private void onStartGame(ActionEvent event) {
    // Play a sound when the button is clicked
    App.stopSound();
    App.playSound("button.mp3");
    System.out.println("Game started!");
    try {
      // Open the backstory scene and set the last scene to the start game scene
      App.openBackstory(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
