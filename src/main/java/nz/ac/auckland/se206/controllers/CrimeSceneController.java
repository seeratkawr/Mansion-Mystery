package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import statement
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class CrimeSceneController {

  // set the game state to game started
  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;

  @FXML
  private void onMapClicked(MouseEvent event) {

    try {
      // Open the map view
      App.openMap(event, "/images/Study.png");
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  @FXML
  private void onLaptopClicked(MouseEvent event) {
    try {
      App.openLaptopClue(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
