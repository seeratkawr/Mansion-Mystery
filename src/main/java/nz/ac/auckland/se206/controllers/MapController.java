package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

// nz.ac
// nz.ac.auckland.se206.controllers.MapController
public class MapController {

  @FXML private Button exit_button;
  @FXML private ImageView background;
  private Scene lastScene; // Field to store the last scene

  public void setLastScene(Scene scene) {
    this.lastScene = scene;
  }

  public void changeBackground(String path) {
    background.setImage(new Image(path));
  }

  @FXML
  private void onExitClicked(ActionEvent event) {
    try {
      // Navigate back to the last scene
      App.openCrimeScene(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Exit Map clicked");
  }

  @FXML
  private void onKitchenClicked(MouseEvent event) {
    try {
      // Open the kitchen view
      System.out.println("Kitchen clicked");
      App.openKitchen(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  @FXML
  private void onDaughterClicked(MouseEvent event) {
    try {
      // Open the daughter view
      App.openSuspectDaughter(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  @FXML
  private void onCleanerClicked(MouseEvent event) {
    try {
      // Open the cleaner view
      App.openSuspectCleaner(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }
}
