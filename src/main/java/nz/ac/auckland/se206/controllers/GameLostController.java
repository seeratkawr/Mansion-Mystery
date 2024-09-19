package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

/** Controller class for handling the game lost screen. */
public class GameLostController {

  // Reference to the restart button in the FXML file
  @FXML private Button restartButton;

  /**
   * Method called when the restart button is clicked.
   *
   * @param event the action event triggered by clicking the button
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void onRestartClicked(ActionEvent event) throws IOException {
    // Call the method to restart the game
    App.restartGame(event);
  }
}
