package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

// Controller class for handling the James Clue scene
public class JamesClueController {
  // FXML injected Rectangle for button area
  @FXML private Rectangle rectangleButtons;
  // FXML injected Label for displaying the timer
  @FXML private Label timerLabel;

  // Method to handle the close clue action
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
    // Open the laptop scene
    App.openLaptop(event);
  }

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }
}
