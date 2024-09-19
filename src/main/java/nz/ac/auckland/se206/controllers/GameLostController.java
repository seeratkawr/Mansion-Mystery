package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

// Controller class for handling the game lost scenario
public class GameLostController {
  // Reference to the restart button in the FXML file
  @FXML private Button restartButton;

  // Method to handle the restart button click event
  @FXML
  private void onRestartClicked(ActionEvent event) throws IOException {
    // Call the method to restart the game
    App.restartGame(event);
  }
}
