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
import nz.ac.auckland.se206.Time;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GuessingController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private Rectangle chef;
  @FXML private Rectangle cleaner;
  @FXML private Rectangle daughter;
  @FXML private Button btnSubmit;
  @FXML private TextField txtInput;
  @FXML private Label lbSelectedSuspect;
  @FXML private Label lbSelected;
  @FXML private Label lbTimesUp;
  @FXML private Rectangle rectangleBackground;
  @FXML private Button btnResults;
  @FXML private Label lbTimer;

  private String chosenSuspect;
  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private List<Thread> threads = new ArrayList<Thread>();
  Timer timer = new Timer();

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

    rectangleBackground.setVisible(false);
    lbTimesUp.setDisable(true);
    lbTimesUp.setVisible(false);
    btnResults.setDisable(true);
    btnResults.setVisible(false);
    setProfession("AI");

    // start 60 second timer for users to guess
    startTimer(0, 30);
  }

  /**
   * This method starts the timer for guessing. When the timer ends
   * the user will be prompted to see the results and be taken to the
   * game over screen.
   *
   * @param minutes the number of minutes for the timer
   * @param seconds the number of seconds for the timer
   */
  private void startTimer(int minutes, int seconds) {
    Task<Void> backgroundTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Time time = new Time(minutes, seconds);
        TimerTask task = new TimerTask() {

          @Override
          public void run() {
            time.decrementTime();

            // update the timer label every second if the time is not 0
            if(!(time.getMinutes() == 0 && time.getSeconds() == 0)) {
              Platform.runLater(() -> {
                lbTimer.setText("Time remaining: " + time.toString());
              });
            } else {
              // if the time is 0, cancel the timer and show the results button
              timer.cancel();
              lbTimesUp.setDisable(false);
              lbTimesUp.setVisible(true);
              btnResults.setDisable(false);
              btnResults.setVisible(true);
              rectangleBackground.setVisible(true);
              lbTimer.setVisible(false);
              App.setAiGameResult("You did not guess, you ran out of time!");
            }
          }
        };
        // schedule the timer to run every second
        timer.scheduleAtFixedRate(task, 0, 1000);
        return null;
      }
    };

    Thread backgroundThread = new Thread(backgroundTask);
    threads.add(backgroundThread); // add thread to list of active threads
    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }

  /**
   * This method is called when the user clicks on the chef rectangle.
   * It sets the chosen suspect to chef and updates the selected suspect label.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickedChef(MouseEvent event) {
    chosenSuspect = "chef";
    updateSelectedSuspect(chosenSuspect);
  }

  /**
   * This method is called when the user clicks on the cleaner rectangle.
   * It sets the chosen suspect to cleaner and updates the selected suspect label.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickedCleaner(MouseEvent event) {
    chosenSuspect = "cleaner";
    updateSelectedSuspect(chosenSuspect);
  }

  /**
   * This method is called when the user clicks on the daughter rectangle.
   * It sets the chosen suspect to daughter and updates the selected suspect label.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickedDaughter(MouseEvent event) {
    chosenSuspect = "daughter";
    updateSelectedSuspect(chosenSuspect);
  }

  /**
   * This method is called when the user clicks the submit button.
   * It will submit the user's guess and run the AI chat operation.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onSubmitMessage(ActionEvent event) {

    // sending the user's guess to the AI
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      System.err.println("cannot submit empty message");
      return;
    }
    
    // clean up and cancel threads
    cleanUpThreads();
    System.out.println("Submit message clicked");
    lbTimer.setVisible(false);

    txtInput.clear();
    ChatMessage userMessage = new ChatMessage("user", "the user guessed " + chosenSuspect +": " + message);

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
                cleanUpThreads();
                App.openGameOver(event);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
        return null;
      }
    };
    Thread thread = new Thread(task);
    threads.add(thread); // add thread to list of active threads
    thread.setDaemon(true);
    thread.start();
  }

  /**
   * This method is called when the user clicks the see results button.
   * This button is presented when the user runs out of time.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickSeeResults(ActionEvent event) {
    try {
      cleanUpThreads();
      App.openGameOver(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method sets the profession of the AI.
   * It will set the system prompt based on the profession.
   *
   * @param profession the profession of the AI
   */
  public void setProfession(String profession) {
    this.profession = profession;

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest = new ChatCompletionRequest(config)
          .setN(1)
          .setTemperature(0.2)
          .setTopP(0.5)
          .setMaxTokens(100);

      // run chat operation in a background thread
      Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          ChatMessage systemMessage = new ChatMessage("system", getSystemPrompt());
          ChatMessage response = runGpt(systemMessage);

          return null;
        }
      };

      Thread thread = new Thread(task);
      threads.add(thread); // add thread to list of active threads
      thread.setDaemon(true);
      thread.start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method runs the GPT chat operation.
   *
   * @param msg the message to send to the AI
   * @return the response from the AI
   * @throws ApiProxyException if the API request fails
   */
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

  /**
   * This method gets the system prompt based on the profession of the AI.
   *
   * @return the system prompt
   */
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

  /**
   * This method updates the selected suspect label with the chosen suspect.
   *
   * @param chosenSuspect the chosen suspect
   */
  private void updateSelectedSuspect(String chosenSuspect) {
    lbSelectedSuspect.setText(chosenSuspect);
    showTextField();
  }

  /**
   * This method shows the text field and submit button.
   */
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

  /**
   * This method cleans up and cancels all active threads.
   */
  private void cleanUpThreads() {
    timer.cancel();
    if(!threads.isEmpty()) {
      for (Thread thread : threads) {
        thread.interrupt();
      }
      threads.clear();
    }
  }
}
