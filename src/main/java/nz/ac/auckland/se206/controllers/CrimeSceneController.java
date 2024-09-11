package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import statement
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrimeSceneController {

  // set the game state to game started
  @FXML ImageView map;

  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      // Load the new FXML file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
      Parent root = loader.load();

      // Get the current stage and set the new scene
      Scene scene = new Scene(root);
      Stage stage = (Stage) map.getScene().getWindow();

      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }
}
