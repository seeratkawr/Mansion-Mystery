package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;

/**
 * This class is the controller for the Safe.fxml file. It contains the logic for the safe scene.
 */
public class SafeController {

  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private AnchorPane safePane;

  /** This method initializes the controller. */
  @FXML
  void initialize() {
    assert safePane != null
        : "fx:id=\"crimeScenePane\" was not injected: check your FXML file 'Safe.fxml'.";
  }

  /**
   * This method is called when the user clicks the fingerprint.
   *
   * @param event the event that triggered this method
   */
  @FXML
  void onClickedFingerprint(MouseEvent event) {
    System.out.println("Fingerprint clicked");
  }

  /**
   * This method is called when the user clicks the keypad.
   *
   * @param event the event that triggered this method
   * @throws IOException
   */
  @FXML
  void onClickedKeypad(MouseEvent event) throws IOException {
    System.out.println("Keypad clicked");
    App.openSafeKeypad(event);
  }

  /**
   * This method is called when the user clicks the back button. It returns users to the crime
   * scene.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onGoBackCrimeScene(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    App.openCrimeScene(event);
  }
}
