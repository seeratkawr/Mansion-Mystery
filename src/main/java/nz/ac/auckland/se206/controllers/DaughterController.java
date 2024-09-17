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

// nz.ac.auckland.se206.controllers.DaughterController
public class DaughterController {
  @FXML private Button btnSend;
  @FXML private TextField txtInput;
  @FXML private TextArea daughterText;
  @FXML private ImageView loadingIndicator;

  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition translateTransition;

  public void initialize() {
    loadingIndicator.setVisible(false);
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/bear.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(0);
    translateTransition.setToX(250);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    daughterText.setEditable(false);
    daughterText.setWrapText(true);

    setProfession("Daughter");
  }

  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.openMap(event, "/images/bedroom.jpg");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onSendMessage(ActionEvent event) {
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

    new Thread(task).start();
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
              .setTopP(0.5)
              .setMaxTokens(30);

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
      new Thread(task).start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  private void appendChatMessage(ChatMessage msg) {
    daughterText.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
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
    daughterText.clear();
  }

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
