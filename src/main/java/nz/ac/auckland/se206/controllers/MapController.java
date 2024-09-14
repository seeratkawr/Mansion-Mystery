package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

  public void changeBackground(String path) {
    background.setImage(new Image(path));
  }

  @FXML
  private void onExitClicked(ActionEvent event) {
    try {
      // Open the crime scene view
      App.openCrimeScene(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
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
}
