package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/**
 * The KitchenController class is responsible for handling the interactions within the kitchen scene
 * of the application. It manages the chat interface, user input, and animations related to the
 * kitchen scene.
 */
public class KitchenController {
  private static boolean initialMessageShown = false;

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private ImageView loadingIndicator;
  @FXML private ImageView mapImage;
  @FXML private Label lbPopup;
  @FXML private Label lbPopup2;
  @FXML private Label timerLabel;

  private String profession;
  private TranslateTransition translateTransition;

  /** Initializes the controller class. This method is called after the FXML fields are injected. */
  public void initialize() {
    loadingIndicator.setVisible(false); // Hide loading indicator initially
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/spatula.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0); // Start position (off-screen)
    translateTransition.setToX(310); // End position (adjust as needed)
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE); // Loop the animation
    translateTransition.setAutoReverse(true); // Move back and forth
    txtaChat.setWrapText(true); // Enable text wrapping in chat area
    txtaChat.setEditable(false);

    // Display the initial message
    if (!initialMessageShown) {
      App.initialTypedOutMessage(
          "Hi detective! I'm John, the chef here. Nice to meet you! What brings you here?",
          txtaChat);
      initialMessageShown = true;
    } else {
      App.initialTypedOutMessage(
          "Oh, hey again! Did you need anything else from me?",
          txtaChat);
    }

    App.setProfession("Chef", txtaChat, loadingIndicator, translateTransition);
    profession = App.getCurrentProfession();

    // Add event handler for the Enter key to send the message
    txtInput.setOnKeyPressed(
        event -> {
          switch (event.getCode()) {
            case ENTER:
              btnSend.fire(); // Trigger the send button programmatically
              break;
            default:
              break;
          }
        });
  }

  /**
   * Method to get the chat area.
   *
   * @return The chat area
   */
  public Label getTimerLabel() {
    return timerLabel;
  }
  
  /**
   * Setter method for the initial message shown flag to indicate if the initial message is shown.
   *
   * @return The initial message shown flag
   */
  public void setInitialMessageShown(boolean initialMessageShown) {
    KitchenController.initialMessageShown = initialMessageShown;
  }
  
  /**
   * Method to handle the event when the user clicks on the map image.
   *
   * @param event The mouse event that triggered the method
   */
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      // Play sound and open map scene
      App.playSound("map.mp3");
      App.openMap(event, "/images/Kitchen.png");
      MapController.setLastScene("kitchen");
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Map clicked");
  }

  /**
   * Method to handle the event when the user clicks on the send button to send a message.
   *
   * @param event The action event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws IOException {
    App.handleGpt(profession, txtInput, txtaChat, loadingIndicator, translateTransition, null);
  }

  /**
   * Method to handle the event when the user clicks on the submit button to make a guess.
   *
   * @param event The action event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.handleGuess(lbPopup, lbPopup2);
  }
}
