package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

// nz.ac.auckland.se206.controllers.GameLostController
public class GameLostController {
  @FXML private Button restartButton;

  @FXML
  private void onRestartClicked(ActionEvent event) throws IOException {
    App.restartGame(event);
  }
}
