package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
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
import nz.ac.auckland.se206.TimerUtility;
import nz.ac.auckland.se206.prompts.PromptEngineering;

// nz.ac.auckland.se206.controllers.DaughterController
public class DaughterController {
  @FXML private Button btnSend;
  @FXML private TextField txtInput;
  @FXML private TextArea daughterText;
  @FXML private ImageView loadingIndicator;
  @FXML private Label timerLabel;

  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition translateTransition;
  private TimerUtility timer;

  // Initialize the controller
  public void initialize() {
    // Set up the loading indicator
    loadingIndicator.setVisible(false);
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/bear.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0);
    translateTransition.setToX(316);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);

    // Set up the text area
    daughterText.setEditable(false);
    daughterText.setWrapText(true);

    // Set the profession to "Daughter"
    setProfession("Daughter");

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

  // Set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

    // Create an AnimationTimer to update the timer label
    AnimationTimer timerAnimation =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            timerLabel.setText(timer.formatTime(timer.getSecondsLeft()));
          }
        };

    // Start the AnimationTimer
    timerAnimation.start();
  }

  // Get the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Handle the map click event
  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.openMap(event, "/images/bedroom.jpg");
      MapController.setLastScene("daughter");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Handle the send message button click event
  @FXML
  private void onSendMessage(ActionEvent event) {
    App.playSound("button.mp3");
    App.addSuspectTalkedTo(profession);
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }

    clearChat();

    txtInput.clear();
    ChatMessage userMessage = new ChatMessage("user", message);
    appendChatMessage(userMessage);

    loadingIndicator.setVisible(true);
    translateTransition.play();

    // Create a task to run the GPT model
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage response = runGpt(userMessage);
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

  // Set the profession and initialize the chat
  public void setProfession(String profession) {
    this.profession = profession;
    clearChat();

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

      // Create a task to initialize the chat with the system prompt
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              ChatMessage systemMessage = new ChatMessage("system", getSystemPrompt());
              ChatMessage response = runGpt(systemMessage);
              Platform.runLater(
                  () -> {
                    appendChatMessage(response);
                    loadingIndicator.setVisible(false);
                    translateTransition.stop();
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

  // Append a chat message to the text area
  private void appendChatMessage(ChatMessage msg) {
    daughterText.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
    System.out.println(
        "Response from LLM: " + msg.getContent()); // Print the response to the console
  }

  // Run the GPT model with the given message
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Clear the chat text area
  private void clearChat() {
    daughterText.clear();
  }

  // Get the system prompt based on the profession
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);

    String promptFileName;
    if ("Daughter".equals(profession)) {
      promptFileName = "daughter_prompt.txt";
    } else {
      throw new IllegalStateException("Unexpected profession: " + profession);
    }

    String prompt = PromptEngineering.getPrompt(promptFileName, map);
    return prompt;
  }
}
