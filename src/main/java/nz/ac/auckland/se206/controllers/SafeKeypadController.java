package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class SafeKeypadController {

  // FXML injected fields
  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private AnchorPane safePane;
  @FXML private Label timerLabel;
  @FXML private Rectangle rectangle1;
  @FXML private Rectangle rectangle2;
  @FXML private Rectangle rectangle3;
  @FXML private Rectangle rectangle4;
  @FXML private Rectangle rectangle5;
  @FXML private Rectangle rectangle6;
  @FXML private Rectangle rectangle7;
  @FXML private Rectangle rectangle8;
  @FXML private Rectangle rectangle9;
  @FXML private Rectangle rectangle0;
  @FXML private Rectangle rectangleClear;

  // List to store the entered code
  List<Integer> code = new ArrayList<>();
  // The correct password to open the safe
  List<Integer> password = new ArrayList<>(Arrays.asList(5, 3, 1));
  // The length of the code
  int codeLength = password.size();

  /**
   * Gets the timer label.
   *
   * @return the timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /** This method initializes the controller. */
  @FXML
  void initialize() {
    // Reset the user entry code when the scene is loaded
    code.clear();
  }

  /**
   * This method is called when the user clicks the go back button. It returns users to the safe
   * scene.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onGoBackSafe(MouseEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to crimescene");
    App.openSafe(event);
  }

  /**
   * This method validates the code entered by the user. If the code is correct, the safe will open.
   * A beeping sound is played to indicate the code is being entered. If the code is incorrect, an
   * error sound is played and the code is cleared. The code must be 4 digits long. If the code is
   * correct, the sound of the safe opening is played.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  void validateCode(MouseEvent event) throws IOException {
    // Play a beep sound when a keypad button is clicked
    App.playSound("safeKeypadBeep.mp3");

    // Validate the code only if it is the correct length
    if (code.size() == codeLength) {
      // Play the sound of the safe opening in a background thread when the correct
      // code is entered to prevent blocking the UI thread when switching scenes
      if (code.equals(password)) {
        App.playSound("safeOpen.mp3");
        System.out.println("Correct code entered");
        App.openSafeOpened(event);
      } else {
        // Play an error sound and clear the code if the code is incorrect
        App.playSound("safeKeypadError.mp3");
        System.out.println("Incorrect code entered");
        code.clear();
      }
    }
  }

  /**
   * This method is called when the user clicks the clear button. It clears the code entered by the
   * user.
   *
   * @param event the event that triggered this method
   */
  @FXML
  void onClickedClear(MouseEvent event) {
    App.playSound("safeKeypadBeep.mp3");
    System.out.println("clearing code: +" + code);
    code.clear();
    System.out.println("code cleared");
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedZero(MouseEvent event) throws IOException {
    code.add(0);
    System.out.println("0");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedOne(MouseEvent event) throws IOException {
    code.add(1);
    System.out.println("1");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedTwo(MouseEvent event) throws IOException {
    code.add(2);
    System.out.println("2");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedThree(MouseEvent event) throws IOException {
    code.add(3);
    System.out.println("3");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedFour(MouseEvent event) throws IOException {
    code.add(4);
    System.out.println("4");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedFive(MouseEvent event) throws IOException {
    code.add(5);
    System.out.println("5");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedSix(MouseEvent event) throws IOException {
    code.add(6);
    System.out.println("6");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedSeven(MouseEvent event) throws IOException {
    code.add(7);
    System.out.println("7");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedEight(MouseEvent event) throws IOException {
    code.add(8);
    System.out.println("8");
    validateCode(event);
  }

  /**
   * This method is called when the user clicks a button on the keypad. The number of the button is
   * added to the code and the code is validated.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  void onClickedNine(MouseEvent event) throws IOException {
    code.add(9);
    System.out.println("9");
    validateCode(event);
  }
}
