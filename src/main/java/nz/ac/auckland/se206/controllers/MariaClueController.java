package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;

/** Controller class for handling the Maria Clue screen. */
public class MariaClueController {

  // FXML injected Label element for displaying the timer
  @FXML private Label timerLabel;
  // FXML injected Circle elements for the buttons
  @FXML private Circle circleButton1;
  @FXML private Circle circleButton2;
  @FXML private Circle circleButton3;

  /**
   * Initializes the controller class. This method is called after the FXML fields are injected.
   *
   * @param event The mouse event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    // Open the laptop scene
    App.openSuspectLaptop(event);
    // Play mouse click sound
    App.playSound("mouseclick.mp3");
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
