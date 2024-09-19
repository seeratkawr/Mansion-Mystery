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

// Controller class for the Daughter scene
public class DaughterController {
  @FXML private Button btnSend; // Button to send the message
  @FXML private TextField txtInput; // Text field for user input
  @FXML private TextArea daughterText; // Text area to display chat messages
  @FXML private ImageView loadingIndicator; // Loading indicator image
  @FXML private Label timerLabel; // Label to display the timer

  private String profession; // Profession of the character
  private ChatCompletionRequest chatCompletionRequest; // Request object for chat completion
  private TranslateTransition translateTransition; // Animation for loading indicator

  // Initialize method called after the FXML fields are populated
  public void initialize() {
    loadingIndicator.setVisible(false); // Hide loading indicator initially
    loadingIndicator.setImage(
        new Image(
            getClass().getResourceAsStream("/images/bear.png"))); // Set loading indicator image
    translateTransition =
        new TranslateTransition(
            Duration.seconds(2), loadingIndicator); // Create translate transition animation
    translateTransition.setFromX(0);
    translateTransition.setToX(316);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    daughterText.setEditable(false); // Make text area non-editable
    daughterText.setWrapText(true); // Enable text wrapping

    setProfession("Daughter"); // Set the profession to Daughter

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
      App.openMap(event, "/images/bedroom.jpg"); // Open the map with the specified image
      MapController.setLastScene("daughter"); // Set the last scene to daughter
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Event handler for send button click
  @FXML
  private void onSendMessage(ActionEvent event) {
    App.playSound("button.mp3"); // Play button click sound
    App.addSuspectTalkedTo(profession); // Add the profession to the list of suspects talked to
    String message = txtInput.getText().trim(); // Get the user input message
    if (message.isEmpty()) {
      return; // Do nothing if the message is empty
    }

    clearChat(); // Clear the chat

    txtInput.clear(); // Clear the input field
    ChatMessage userMessage = new ChatMessage("user", message); // Create a new chat message
    appendChatMessage(userMessage); // Append the user message to the chat

    loadingIndicator.setVisible(true); // Show loading indicator
    translateTransition.play(); // Start loading animation

    // Create a task to run the GPT model
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage response = runGpt(userMessage); // Get the response from GPT
            Platform.runLater(
                () -> {
                  appendChatMessage(response); // Append the response to the chat
                  loadingIndicator.setVisible(false); // Hide loading indicator
                  translateTransition.stop(); // Stop loading animation
                });
            return null;
          }
        };

    Thread thread = new Thread(task); // Create a new thread for the task
    App.addThread(thread); // Add the thread to the app
    thread.setDaemon(true); // Set the thread as a daemon
    thread.start(); // Start the thread
  }

  // Method to set the profession
  public void setProfession(String profession) {
    this.profession = profession; // Set the profession
    clearChat(); // Clear the chat

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig(); // Read the API proxy config
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.4)
              .setMaxTokens(100); // Set chat completion request parameters

      loadingIndicator.setVisible(true); // Show loading indicator
      translateTransition.play(); // Start loading animation

      // Create a task to run the GPT model
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              ChatMessage systemMessage =
                  new ChatMessage("system", getSystemPrompt()); // Create system message
              ChatMessage response = runGpt(systemMessage); // Get the response from GPT
              Platform.runLater(
                  () -> {
                    appendChatMessage(response); // Append the response to the chat
                    loadingIndicator.setVisible(false); // Hide loading indicator
                    translateTransition.stop(); // Stop loading animation
                  });
              return null;
            }
          };
      Thread thread = new Thread(task); // Create a new thread for the task
      App.addThread(thread); // Add the thread to the app
      thread.setDaemon(true); // Set the thread as a daemon
      thread.start(); // Start the thread
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  // Method to append a chat message to the text area
  private void appendChatMessage(ChatMessage msg) {
    daughterText.appendText(msg.getContent() + "\n\n"); // Append message to text area
    System.out.println(
        "Response from LLM: " + msg.getContent()); // Print the response to the console
  }

  // Method to run the GPT model and get a response
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg); // Add the message to the request
    try {
      ChatCompletionResult chatCompletionResult =
          chatCompletionRequest.execute(); // Execute the request
      Choice result = chatCompletionResult.getChoices().iterator().next(); // Get the result choice
      chatCompletionRequest.addMessage(
          result.getChatMessage()); // Add the result message to the request
      return result.getChatMessage(); // Return the result message
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Method to clear the chat text area
  private void clearChat() {
    daughterText.clear(); // Clear the text area
  }

  // Method to get the system prompt based on the profession
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession); // Add profession to the map

    String promptFileName;
    if ("Daughter".equals(profession)) {
      promptFileName = "daughter_prompt.txt"; // Set prompt file name for Daughter
    } else {
      throw new IllegalStateException(
          "Unexpected profession: " + profession); // Throw exception for unexpected profession
    }

    String prompt =
        PromptEngineering.getPrompt(promptFileName, map); // Get the prompt from the file
    return prompt; // Return the prompt
  }
}
