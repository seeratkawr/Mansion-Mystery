package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;

/**
 * Controller class for handling the James Clue screen.
 */
public class JamesClueController {

  // FXML injected Label for displaying the timer
  @FXML private Label timerLabel;
  // FXML injected Circle elements for the buttons
  @FXML private Circle circleButton1;
  @FXML private Circle circleButton2;
  @FXML private Circle circleButton3;

  /**
   * Initializes the controller class. This method is called after the FXML fields are injected.
   */
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
    // Open the laptop scene
    App.openSuspectLaptop(event);
  }

  /**
   * Getter for the timer label.
   *
   * @return The timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }
}
