package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import statement
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class CrimeSceneController {

  // set the game state to game started
  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;
  @FXML private Rectangle bookshelfSafeRectangle;
  @FXML private Button guessingButton;

  @FXML
  private void onMapClicked(MouseEvent event) {

    try {
      // Open the map view
      App.playSound("mapunfolding.mp3");
      App.openMap(event, "/images/Study.png");
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  @FXML
  private void onKitchenClicked(MouseEvent event) {
    try {
      // Open the kitchen view
      App.openKitchen(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  @FXML
  private void onLaptopClicked(MouseEvent event) {
    try {
      App.openLaptop(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onDrawersClicked(MouseEvent event) {
    try {
      // Open the drawer view
      App.playSound("draweropen.mp3");
      App.openDrawer(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the bookshelf safe.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onBookshelfSafeClicked(MouseEvent event) {
    try {
      App.openSafe(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onGuessClicked(ActionEvent event) {
    System.out.println("Guessing button clicked");
  }
}
