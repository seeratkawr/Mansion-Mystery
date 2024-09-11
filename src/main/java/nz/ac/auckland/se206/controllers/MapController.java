package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// nz.ac
// nz.ac.auckland.se206.controllers.MapController
public class MapController {
  @FXML ImageView exit_button;

  @FXML
  private void onExitClicked(MouseEvent event) {
    try {
      // Load the new FXML file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/crimescene.fxml"));
      Parent root = loader.load();

      // Get the current stage and set the new scene
      Scene scene = new Scene(root);
      Stage stage = (Stage) exit_button.getScene().getWindow();
      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }
}
