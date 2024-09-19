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

public class CleanerController {
  @FXML private Button btnSend;
  @FXML private TextField txtInput;
  @FXML private TextArea txtaChat;
  @FXML private ImageView loadingIndicator;
  @FXML private Label timerLabel;

  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition translateTransition;
  private TimerUtility timer;

  public void initialize() {
    loadingIndicator.setVisible(false);
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/broom.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0);
    translateTransition.setToX(250);
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

  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

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

  public Label getTimerLabel() {
    return timerLabel;
  }

  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.openMap(event, "/images/cleaner.png");
      MapController.setLastScene("cleaner");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
    Thread thread = new Thread(task);
    App.addThread(thread);
    thread.setDaemon(true);
    thread.start();
  }

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

  private void appendChatMessage(ChatMessage msg) {
    txtaChat.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
    System.out.println(
        "Response from LLM: " + msg.getContent()); // Print the response to the console
  }

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

  private void clearChat() {
    txtaChat.clear();
  }

  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);

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
