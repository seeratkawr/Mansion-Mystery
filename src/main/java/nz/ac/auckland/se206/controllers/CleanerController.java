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
  private ChatCompletionRequest chatCompletionRequest; // Request object for chat completion
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

    setProfession("Cleaner");

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
    App.playSound("button.mp3");
    // Add the profession to the list of suspects talked to
    App.addSuspectTalkedTo(profession);
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }

    // Clear the chat area and append the user message
    clearChat();

    txtInput.clear();
    ChatMessage userMessage = new ChatMessage("user", message); // Create a user message
    appendChatMessage(userMessage); //  Append the user message to the chat area

    loadingIndicator.setVisible(true);
    translateTransition.play();

    // Task to handle chat completion request in a background thread
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage response = runGpt(userMessage);

            // Append the response to the chat area and stop the loading indicator
            Platform.runLater(
                () -> {
                  appendChatMessage(response);
                  loadingIndicator.setVisible(false);
                  translateTransition.stop();
                });
            return null;
          }
        };

    // Start the task in a new thread
    Thread thread = new Thread(task);
    App.addThread(thread);
    thread.setDaemon(true);
    thread.start();
  }

  // Method to set the profession and initialize chat completion request
  public void setProfession(String profession) {
    this.profession = profession;
    clearChat(); // Clear the chat area

    // Initialize chat completion request
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.4)
              .setMaxTokens(100);

      loadingIndicator.setVisible(true);
      translateTransition.play();

      // Task to handle initial chat completion request in a background thread
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Create a system message and get the response from GPT
              ChatMessage systemMessage = new ChatMessage("system", getSystemPrompt());
              ChatMessage response = runGpt(systemMessage);

              // On a new thread, append the response to the
              // chat area and stop the loading indicator
              Platform.runLater(
                  () -> {
                    appendChatMessage(response);
                    loadingIndicator.setVisible(false);
                    translateTransition.stop();
                  });
              return null;
            }
          };

      // Start the task in a new thread
      Thread thread = new Thread(task);
      App.addThread(thread);
      thread.setDaemon(true);
      thread.start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  // Method to append a chat message to the chat area
  private void appendChatMessage(ChatMessage msg) {
    txtaChat.appendText(msg.getContent() + "\n\n");
    System.out.println(
        "Response from LLM: " + msg.getContent()); // Print the response to the console
  }

  // Method to run GPT chat completion request
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      // Execute the chat completion request and get the response
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Method to clear the chat area
  private void clearChat() {
    txtaChat.clear();
  }

  // Method to get the system prompt based on the profession
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);

    // Get the prompt from the file based on the profession
    String promptFileName;
    if ("Cleaner".equals(profession)) {
      promptFileName = "cleaner_prompt.txt";
    } else {
      throw new IllegalStateException("Unexpected profession: " + profession);
    }

    String prompt = PromptEngineering.getPrompt(promptFileName, map);
    return prompt;
  }
}
