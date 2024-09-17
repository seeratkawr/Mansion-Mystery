package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class GuessingController {

  @FXML
  private void onClickedSuspect(MouseEvent event) {
    // Open the suspect view
    System.out.println("Suspect clicked");
  }
}
