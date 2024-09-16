package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

// nz.ac.auckland.se206.controllers.DaughterController
public class DaughterController {
  @FXML private Button sendButton;
  @FXML private TextField textInput;
  @FXML private TextArea daughterText;

  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private TranslateTransition translateTransition;

  public void initialize() {
    setProfession("Daughter");
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
          .setMaxTokens(100);
      
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.openMap(event, "/images/bedroom.png"); 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onSendButton(ActionEvent event) {
    String message = textInput.getText().trim();
    if(message.isEmpty()) {
      return;
    }

    clearChat();

    textInput.clear();
    ChatMessage userMessage = new ChatMessage("user", message);
    appendChatMessage(userMessage);

    
    try {
      ChatMessage response = runGpt(userMessage);
    } catch (ApiProxyException e) {
      // TODO Auto-generated catch block
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
  
}
