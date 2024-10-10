package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;

/*
 * Controller class for handling the map screen.
 */
public class MapController {

  // Field to store the last scene
  private static String lastScene;

  // Static method to set the last scene
  public static void setLastScene(String scene) {
    lastScene = scene;
  }

  // FXML injected fields
  @FXML private ImageView background;
  @FXML private Label timerLabel;
  @FXML private Button btnExit;
  @FXML private Button btnToStudy;
  @FXML private Circle kitchenCircle;
  @FXML private Circle daughterCircle;
  @FXML private Circle cleanerCircle;

  /**
   * Gets the label for the timer on the map screen.
   * 
   * @return The timer label on the map screen
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /**
   * Initializes the controller class. This method is called after the FXML fields are injected.
   * 
   * @param event The mouse event that triggered the method
   */
  public void changeBackground(String path) {
    background.setImage(new Image(path));
  }

  /**
   * Method called when the exit button is clicked.
   * 
   * @param event The action event that triggered the method
   */
  @FXML
  private void onExitClicked(ActionEvent event) {
    try {
      // Go back to the last scene
      App.goLastScene(lastScene);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Exit Map clicked");
  }

  /**
   * Method called when the kitchen area is clicked.
   * 
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onKitchenClicked(MouseEvent event) {
    try {
      // Play door shut sound
      App.playSound("doorshut.mp3");
      // Open the kitchen view
      System.out.println("Kitchen clicked");
      App.openSuspect(event, "kitchen");
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  /**
   * Method called when the daughter area is clicked.
   * 
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onDaughterClicked(MouseEvent event) {
    try {
      // Play door shut sound
      App.playSound("doorshut.mp3");
      // Open the daughter view
      App.openSuspect(event, "daughter");
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  /**
   * Method called when the cleaner area is clicked.
   * 
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onCleanerClicked(MouseEvent event) {
    try {
      // Play door shut sound
      App.playSound("doorshut.mp3");
      // Open the cleaner view
      App.openSuspect(event, "cleaner");
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  /**
   * Method called when the study button is clicked.
   * 
   * @param event The action event that triggered the method
   */
  @FXML
  private void onStudyClicked(ActionEvent event) {
    try {
      // Play door shut sound
      App.playSound("doorshut.mp3");
      // Open the study view
      App.openCrimeScene();
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }
}
