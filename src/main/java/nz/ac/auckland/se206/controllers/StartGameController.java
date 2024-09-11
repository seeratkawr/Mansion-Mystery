package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class StartGameController {

  @FXML private Button startButton;

  @FXML
  private void onStartGame(ActionEvent event) {
    System.out.println("Game started!");
    try {
      App.startGame(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }
}
