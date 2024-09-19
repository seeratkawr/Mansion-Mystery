package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the Cleaner scene. This class handles the chat completion request to the
 * OpenAI API and displays the chat messages in the chat area.
 *
 * <p>It also handles the user input and sends the messages to the API for completion.
 */
public class CleanerController {
  @FXML private Button btnSend; // Button to send messages
  @FXML private TextField txtInput; // TextField for user input
  @FXML private TextArea txtaChat; // TextArea to display chat messages
  @FXML private ImageView loadingIndicator; // ImageView for loading indicator
  @FXML private Label timerLabel; // Label to display timer

  private String profession; // Profession of the character
  private TranslateTransition translateTransition; // Animation for loading indicator

  // Initialize method called after the FXML fields are injected
  public void initialize() {
    loadingIndicator.setVisible(false);
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/broom.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0);
    translateTransition.setToX(292);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    txtaChat.setEditable(false);
    txtaChat.setWrapText(true);

    App.setProfession("Cleaner", txtaChat,loadingIndicator, translateTransition);
    profession = App.getCurrentProfession();
    System.out.println("prfession from app now actually set to: " + profession);

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

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Event handler for map click
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      // Open the map scene and set the last scene to cleaner
      App.openMap(event, "/images/cleaner.png");
      MapController.setLastScene("cleaner");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Event handler for send button click
  @FXML
  private void onSendMessage(ActionEvent event) {
    System.out.println("Send button clicked - handling gpt with profession: " + profession);
    App.handleGPT(profession, txtInput, txtaChat, loadingIndicator, translateTransition);
  }

}
