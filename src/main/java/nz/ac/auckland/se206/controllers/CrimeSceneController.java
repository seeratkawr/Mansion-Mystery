package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

/**
 * Controller class for handling the crime scene screen.
 */
public class CrimeSceneController {

  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;
  @FXML private Rectangle bookshelfSafeRectangle;
  @FXML private Rectangle drawerRectangle;
  @FXML private Button guessingButton;
  @FXML private Label lbPopup;
  @FXML private Label lbPopup2;
  @FXML private Label timerLabel;
  @FXML private Label backstory1;
  @FXML private Label backstory2;
  @FXML private Label backstory3;
  @FXML private Rectangle backstoryRectangle;
  @FXML private ImageView closeButton;

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
  private void onMapClicked(MouseEvent event) {
    try {
      // Open the map scene and set the last scene to the crime scene
      App.playSound("map.mp3");
      App.openMap(event, "/images/Study.png");
      MapController.setLastScene("crimeScene");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method called when the laptop is clicked. Opens the laptop scene.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onLaptopClicked(MouseEvent event) {
    // Add the laptop to the clues viewed list
    App.addCluesViewed("laptop");
    try {
      App.openLaptop(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method called when the drawers are clicked. Opens the drawers scene.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onDrawersClicked(MouseEvent event) {
    // Add the drawers to the clues viewed list
    App.addCluesViewed("drawer");
    try {
      App.playSound("draweropen.mp3");
      App.openDrawer(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method called when the bookshelf safe is clicked. Opens the safe scene.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onBookshelfSafeClicked(MouseEvent event) {
    // Add the safe to the clues viewed list
    App.addCluesViewed("safe");
    try {
      App.openSafe(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method called when the backstory button is clicked. Displays the backstory and disables the
   * guessing button.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.handleGuess(lbPopup, lbPopup2);
  }

  /**
   * Method called when the backstory button is clicked. Displays the backstory and disables the
   * guessing button.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onCloseClicked(MouseEvent event) {

    // Close the backstory and enable the guessing button
    backstory1.setVisible(false);
    backstory2.setVisible(false);
    backstory3.setVisible(false);
    backstoryRectangle.setVisible(false);
    closeButton.setVisible(false);

    // Enable the map and clues
    map.setMouseTransparent(false);
    laptopRectangle.setMouseTransparent(false);
    bookshelfSafeRectangle.setMouseTransparent(false);
    drawerRectangle.setMouseTransparent(false);
    guessingButton.setDisable(false);
  }
}
