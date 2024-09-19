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
 * The KitchenController class is responsible for handling the interactions within the kitchen scene
 * of the application. It manages the chat interface, user input, and animations related to the 
 * kitchen scene.
 * 
 * <p>FXML Annotations:
 * <ul>
 *   <li>{@code @FXML private TextArea txtaChat} - Text area for chat messages.</li>
 *   <li>{@code @FXML private TextField txtInput} - Text field for user input.</li>
 *   <li>{@code @FXML private Button btnSend} - Button to send messages.</li>
 *   <li>{@code @FXML private ImageView loadingIndicator} - Loading indicator image.</li>
 *   <li>{@code @FXML private Label timerLabel} - Label to display the timer.</li>
 * </ul>
 * 
 * <p>Private Fields:
 * <ul>
 *   <li>{@code private String profession} - Profession of the character.</li>
 *   <li>{@code private ChatCompletionRequest chatCompletionRequest} - Request object for chat completion.</li>
 *   <li>{@code private TranslateTransition translateTransition} - Animation for loading indicator.</li>
 * </ul>
 * 
 * <p>Public Methods:
 * <ul>
 *   <li>{@code public void initialize()} - Initialize method called after FXML fields are populated.</li>
 *   <li>{@code public Label getTimerLabel()} - Get the timer label.</li>
 *   <li>{@code public void setProfession(String profession)} - Set the profession and initialize chat.</li>
 * </ul>
 * 
 * <p>Private Methods:
 * <ul>
 *   <li>{@code private void onMapClicked(MouseEvent event)} - Handle map click event.</li>
 *   <li>{@code private void onSendMessage(ActionEvent event)} - Handle send message button click event.</li>
 *   <li>{@code private void appendChatMessage(ChatMessage msg)} - Append a chat message to the chat area.</li>
 *   <li>{@code private ChatMessage runGpt(ChatMessage msg)} - Run GPT model to get a response.</li>
 *   <li>{@code private String getSystemPrompt()} - Get the system prompt based on the profession.</li>
 *   <li>{@code private void clearChat()} - Clear the chat area.</li>
 * </ul>
 */
public class KitchenController {

  @FXML private TextArea txtaChat; // Text area for chat messages
  @FXML private TextField txtInput; // Text field for user input
  @FXML private Button btnSend; // Button to send messages
  @FXML private ImageView loadingIndicator; // Loading indicator image
  @FXML private Label timerLabel; // Label to display the timer

  private String profession; // Profession of the character
  private ChatCompletionRequest chatCompletionRequest; // Request object for chat completion
  private TranslateTransition translateTransition; // Animation for loading indicator

  // Initialize method called after FXML fields are populated
  public void initialize() {
    loadingIndicator.setVisible(false); // Hide loading indicator initially
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/spatula.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0); // Start position (off-screen)
    translateTransition.setToX(324); // End position (adjust as needed)
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE); // Loop the animation
    translateTransition.setAutoReverse(true); // Move back and forth
    txtaChat.setWrapText(true); // Enable text wrapping in chat area

    // Set profession after transition is set up
    setProfession("Chef");

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

  // Get the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Handle map click event
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.openMap(event, "/images/Kitchen.png");
      MapController.setLastScene("kitchen");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Map clicked");
  }

  // Handle send message button click event
  @FXML
  private void onSendMessage(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Send message clicked");
    App.addSuspectTalkedTo(profession);
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }

    // Clear the chat before sending new message
    clearChat();

    txtInput.clear();
    ChatMessage userMessage = new ChatMessage("user", message);
    appendChatMessage(userMessage);

    // Show the loading indicator and start animation before processing
    loadingIndicator.setVisible(true);
    translateTransition.play(); // Start the animation

    // Run the AI chat operation in a background thread
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage response = runGpt(userMessage);
            Platform.runLater(
                () -> {
                  appendChatMessage(response);
                  loadingIndicator.setVisible(false); // Hide loading indicator after response
                  translateTransition.stop(); // Stop the animation
                });
            return null;
          }
        };
    Thread thread = new Thread(task);
    App.addThread(thread);
    thread.setDaemon(true);
    thread.start();
  }

  // Set the profession and initialize chat
  public void setProfession(String profession) {
    this.profession = profession;
    clearChat(); // Clear chat initially

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.4)
              .setMaxTokens(100);

      // Show loading indicator before fetching system prompt
      loadingIndicator.setVisible(true);
      translateTransition.play(); // Start the animation

      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              ChatMessage systemMessage = new ChatMessage("system", getSystemPrompt());
              ChatMessage response = runGpt(systemMessage);
              Platform.runLater(
                  () -> {
                    appendChatMessage(response);
                    loadingIndicator.setVisible(
                        false); // Hide loading indicator after system message
                    translateTransition.stop(); // Stop the animation
                  });
              return null;
            }
          };
      Thread thread = new Thread(task);
      App.addThread(thread);
      thread.setDaemon(true);
      thread.start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  // Append a chat message to the chat area
  private void appendChatMessage(ChatMessage msg) {
    txtaChat.appendText(msg.getContent() + "\n\n");
    System.out.println(
        "Response from LLM: " + msg.getContent()); // Print the response to the console
  }

  // Run GPT model to get a response
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);

    // Execute the chat completion request and get the response
    try {
      // Get the first choice from the response
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
      return null;
    }
  }

  // Get the system prompt based on the profession
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);

    // Determine the prompt file based on the profession
    String promptFileName;
    if ("Chef".equals(profession)) {
      promptFileName = "chef_prompt.txt"; // Ensure this file exists and is correctly configured
    } else {
      throw new IllegalStateException("Unexpected profession: " + profession);
    }

    String prompt = PromptEngineering.getPrompt(promptFileName, map);
    return prompt;
  }

  // Clear the chat area
  private void clearChat() {
    txtaChat.clear();
  }
}
