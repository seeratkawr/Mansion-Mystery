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
  @FXML private Button btnToStudy;
  private static String lastScene; // Field to store the last scene

  public static void setLastScene(String scene) {
    lastScene = scene;
  }

  public void changeBackground(String path) {
    background.setImage(new Image(path));
  }

  @FXML
  private void onExitClicked(ActionEvent event) {
    try {
      // Navigate back to the last scene
      if (lastScene.equals("daughter")) {
        App.lastSceneDaughter();
      } else if (lastScene.equals("crimeScene")) {
        App.lastSceneCrimeScene();
      } else if (lastScene.equals("kitchen")) {
        App.lastSceneKitchen();
      } else if (lastScene.equals("cleaner")) {
        App.lastSceneCleaner();
      }
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

  @FXML
  private void onStudyClicked(ActionEvent event) {
    try {
      // Open the study view
      App.openCrimeScene(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }
}
