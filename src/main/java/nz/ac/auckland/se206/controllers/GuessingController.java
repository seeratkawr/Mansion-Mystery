package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import java.util.HashMap;
import java.util.Map;

public class GuessingController {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Rectangle chef;
  @FXML
  private Rectangle cleaner;
  @FXML
  private Rectangle daughter;
  @FXML
  private Button btnSubmit;
  @FXML
  private TextField txtInput;
  @FXML
  private Label lbSelectedSuspect;
  @FXML
  private Label lbSelected;

  private String chosenSuspect;
  private String profession;
  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  private void initialize() {

    // set text field and labels to disabled and invisible
    txtInput.setDisable(true);
    lbSelectedSuspect.setDisable(true);
    lbSelected.setDisable(true);
    btnSubmit.setDisable(true);
    txtInput.setVisible(false);
    lbSelectedSuspect.setVisible(false);
    lbSelected.setVisible(false);
    btnSubmit.setVisible(false);
    setProfession("AI");
  }

  @FXML
  private void onClickedChef(MouseEvent event) {
    chosenSuspect = "chef";
    updateSelectedSuspect(chosenSuspect);
  }

  @FXML
  private void onClickedCleaner(MouseEvent event) {
    chosenSuspect = "cleaner";
    updateSelectedSuspect(chosenSuspect);
  }

  @FXML
  private void onClickedDaughter(MouseEvent event) {
    chosenSuspect = "daughter";
    updateSelectedSuspect(chosenSuspect);
  }

  @FXML
  private void onSubmitMessage(ActionEvent event) {
    System.out.println("Submit message clicked");

    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }

    txtInput.clear();
    ChatMessage userMessage = new ChatMessage("user", message);
    // appendChatMessage(userMessage);

    // Run the AI chat operation in a background thread
    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        ChatMessage response = runGpt(userMessage);
        // save the response
        App.setAiGameResult(response.getContent());
        System.out.println("AI response: " + response.getContent());
        Platform.runLater(
            () -> {
              try {
                App.openGameOver(event);
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            });
        return null;
      }
    };
    new Thread(task).start();
  }

  public void setProfession(String profession) {
    this.profession = profession;

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest = new ChatCompletionRequest(config)
          .setN(1)
          .setTemperature(0.2)
          .setTopP(0.5)
          .setMaxTokens(50);

      Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          ChatMessage systemMessage = new ChatMessage("system", getSystemPrompt());
          ChatMessage response = runGpt(systemMessage);
          Platform.runLater(
              () -> {

              });
          return null;
        }
      };
      new Thread(task).start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
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

  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("profession", profession);

    // Determine the prompt file based on the profession
    String promptFileName;
    if ("AI".equals(profession)) {
      promptFileName = "ai_prompt.txt"; // Ensure this file exists and is correctly configured
    } else {
      throw new IllegalStateException("Unexpected profession: " + profession);
    }

    String prompt = PromptEngineering.getPrompt(promptFileName, map);
    return prompt;
  }

  private void updateSelectedSuspect(String chosenSuspect) {
    lbSelectedSuspect.setText(chosenSuspect);
    showTextField();
  }

  private void showTextField() {
    txtInput.setDisable(false);
    txtInput.setVisible(true);
    lbSelectedSuspect.setDisable(false);
    lbSelectedSuspect.setVisible(true);
    lbSelected.setDisable(false);
    lbSelected.setVisible(true);
    btnSubmit.setDisable(false);
    btnSubmit.setVisible(true);
  }

}
