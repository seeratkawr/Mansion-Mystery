package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;

// Controller class for handling the Maria Clue scene
public class MariaClueController {

  // FXML injected Label element for displaying the timer
  @FXML private Label timerLabel;
  // FXML injected Circle elements for the buttons
  @FXML private Circle circleButton1;
  @FXML private Circle circleButton2;
  @FXML private Circle circleButton3;

  // Method to handle the closing of the clue window
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Open the laptop scene
    App.openLaptop(event);
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
  }

  // Getter method for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }
}
