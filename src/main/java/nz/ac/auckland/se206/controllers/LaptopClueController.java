package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

// nz.ac.auckland.se206.controllers.LaptopClueController
public class LaptopClueController {
  @FXML private Button backButton;

  @FXML
  private void onGoBack(ActionEvent event) {
    try {
      // Go back to the crime scene
      App.closeClue(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
