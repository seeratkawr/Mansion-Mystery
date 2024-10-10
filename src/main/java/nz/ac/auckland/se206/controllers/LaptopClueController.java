package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/**
 * Controller class for handling the laptop clue screen.
 */
public class LaptopClueController {
  @FXML private Button backButton; // Button to go back to the previous scene
  @FXML private Label timerLabel; // Label to display the timer
  @FXML private ImageView topLeftDog;
  @FXML private ImageView topRightDog;
  @FXML private ImageView bottomLeftDog;
  @FXML private ImageView bottomRightDog;

  private final Map<ImageView, Double> maxRotations = new HashMap<>();
  private final Map<ImageView, Double> currentRotations = new HashMap<>();

  /**
   * Getter for the timer label.
   * 
   * @return The timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /**
   * Initializes the controller class. This method is called after the FXML fields are injected.
   */
  @FXML
  private void initialize() {
    // Set the maximum rotation for each image
    maxRotations.put(topLeftDog, 90.0);
    maxRotations.put(topRightDog, 180.0);
    maxRotations.put(bottomLeftDog, 180.0);
    maxRotations.put(bottomRightDog, 270.0);

    // Set the current rotation for each image
    currentRotations.put(topLeftDog, 0.0);
    currentRotations.put(topRightDog, 0.0);
    currentRotations.put(bottomLeftDog, 0.0);
    currentRotations.put(bottomRightDog, 0.0);
  }

  /**
   * Method called when the back button is clicked. Closes the current scene and goes back to the
   * 
   * @param event The action event that triggered the method
   */
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) {
    System.out.println("Back button clicked");
    try {
      // Go back to the crime scene
      App.playSound("mouseclick.mp3");
      App.closeClue(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method called when an image is clicked. Rotates the image by 90 degrees if it is not at its max
   * rotation. If all images are at their max rotation, opens the suspect laptop scene.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void rotateImage(MouseEvent event) {
    ImageView clickedImage = (ImageView) event.getSource();

    double currentRotation = currentRotations.get(clickedImage);
    double maxRotation = maxRotations.get(clickedImage);

    // Rotate the image by 90 degrees if it is not at its max rotation
    if (currentRotation < maxRotation) {
      RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), clickedImage);
      rotateTransition.setFromAngle(currentRotation);
      rotateTransition.setToAngle(currentRotation + 90);
      rotateTransition.setOnFinished(
          e -> {
            // Update the current rotation of the image
            currentRotations.put(clickedImage, currentRotation + 90);

            // Open the suspect laptop scene if all images are at their max rotation
            if (allImagesAtMaxRotation()) {
              try {
                App.openSuspectLaptop(event);
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            }
          });
      rotateTransition.play();
    }
  }

  /**
   * Checks if all images are at their max rotation.
   * 
   * @return True if all images are at their max rotation, false otherwise
   */
  private boolean allImagesAtMaxRotation() {
    for (ImageView imageView : maxRotations.keySet()) {

      // If any image is not at its max rotation, return false
      if (currentRotations.get(imageView) < maxRotations.get(imageView)) {
        return false;
      }
    }
    return true;
  }
}
