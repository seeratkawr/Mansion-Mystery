package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

/**
 * The SafeOpenedController class is responsible for handling the interactions and logic for the
 * "safe opened" scene in the application. It manages the timer display and handles user actions
 * such as going back to the previous scene, as well as draggable interactions with the magnifying
 * glass.
 *
 * @see TimerUtility
 * @see App
 */
public class SafeOpenedController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private AnchorPane safePane;
  @FXML private Label timerLabel;
  @FXML private ImageView magnifyingGlass;
  @FXML private Text msgLabel;
  @FXML private Text msgLabel1;

  // Coordinates where the magnifying glass will reveal the hair
  private final double targetX = 364.0;
  private final double targetY = 167.0;

  // Hair image to display when the magnifying glass is in the correct position
  @FXML private ImageView hairImage;

  private double offsetX;
  private double offsetY;

  public Label getTimerLabel() {
    return timerLabel;
  }

  /** This method initializes the controller. */
  @FXML
  void initialize() {
    assert safePane != null
        : "fx:id=\"safePane\" was not injected: check your FXML file 'safeOpened.fxml'.";
    // Hide the hair image initially until the magnifying glass is in the correct
    // position
    makeMagnifyingGlassDraggable();
    hairImage.setVisible(false);
    msgLabel.setVisible(false);
    msgLabel1.setVisible(true);
  }

  /**
   * This method is called when the user clicks the go back button. It returns users to the safe
   * scene.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onGoBackSafe(MouseEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to safe closed");
    App.openSafe(event);
  }

  /**
   * Makes the magnifying glass draggable by setting mouse event handlers to calculate the drag
   * offset and update the position.
   */
  private void makeMagnifyingGlassDraggable() {
    magnifyingGlass.setOnMouseEntered(
        event -> {
          magnifyingGlass.setCursor(javafx.scene.Cursor.MOVE); // Change cursor on hover
          msgLabel1.setVisible(false); // Hide the message when the magnifying glass is moved
        });

    magnifyingGlass.setOnMouseExited(
        event -> {
          magnifyingGlass.setCursor(javafx.scene.Cursor.DEFAULT); // Reset cursor when not hovering
        });

    magnifyingGlass.setOnMousePressed(
        event -> {
          offsetX = event.getSceneX() - magnifyingGlass.getLayoutX();
          offsetY = event.getSceneY() - magnifyingGlass.getLayoutY();
        });

    magnifyingGlass.setOnMouseDragged(
        event -> {
          magnifyingGlass.setLayoutX(event.getSceneX() - offsetX);
          magnifyingGlass.setLayoutY(event.getSceneY() - offsetY);
          checkForHair();
        });
  }

  /**
   * Checks if the magnifying glass is over the target point (334, 115) and shows the hair if the
   * condition is met.
   */
  private void checkForHair() {
    // Get the current position of the magnifying glass
    double currentX = magnifyingGlass.getLayoutX();
    double currentY = magnifyingGlass.getLayoutY();

    // Check if the magnifying glass is close to the target point
    if (Math.abs(currentX - targetX) < 40 && Math.abs(currentY - targetY) < 40) {
      // Show the hair image through the magnifying glass
      hairImage.setVisible(true);
      msgLabel.setVisible(true);
    }
  }
}
