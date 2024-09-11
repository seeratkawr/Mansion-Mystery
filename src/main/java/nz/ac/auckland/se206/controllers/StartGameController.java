package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartGameController {

  @FXML private Button startButton;

  @FXML
  private void onStartGame(ActionEvent event) {
    System.out.println("Game started!");
  }
}
