package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
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

/**
 * The GuessingController class is responsible for handling the user interactions and game logic for
 * the guessing game. It manages the UI components, handles user input, and communicates with the AI
 * to process the user's guesses.
 *
 * <p>Fields: - resources: ResourceBundle for localization. - location: URL for the location of the
 * FXML file. - chef, cleaner, daughter: Rectangles representing the suspects. - btnSubmit: Button
 * for submitting the user's guess. - txtInput: TextField for user input. - lbTimesUp: Label
 * indicating that the time is up. - rectangleBackground: Rectangle for the background. -
 * btnResults: Button for viewing results. - lbTimer: Label for displaying the timer. - circleChef,
 * circleCleaner, circleDaughter: ImageViews for indicating selected suspects. - lbExplain: Label
 * for explanations. - loadingIndicator: ImageView for the loading indicator. - translateTransition:
 * TranslateTransition for animating the loading indicator. - chosenSuspect: String representing the
 * chosen suspect. - profession: String representing the profession of the AI. -
 * chatCompletionRequest: ChatCompletionRequest for AI chat operations. - threads: List of active
 * threads. - timer: TimerUtility for managing the game timer. - guessingTimerCheckTimeline:
 * Timeline for checking if the timer has finished.
 *
 * <p>Methods: - initialize(): Initializes the controller and sets up the UI components. -
 * enableGameOver(): Enables the game over screen. - timeUpCheck(): Checks if the timer has finished
 * and handles the time-up scenario. - onClickedChef(MouseEvent event): Handles the event when the
 * chef rectangle is clicked. - onClickedCleaner(MouseEvent event): Handles the event when the
 * cleaner rectangle is clicked. - onClickedDaughter(MouseEvent event): Handles the event when the
 * daughter rectangle is clicked. - onSubmitMessage(ActionEvent event): Handles the event when the
 * submit button is clicked. - onClickSeeResults(ActionEvent event): Handles the event when the see
 * results button is clicked. - setProfession(String profession): Sets the profession of the AI and
 * initializes the chat request. - runGpt(ChatMessage msg): Runs the GPT chat operation and returns
 * the AI response. - getSystemPrompt(): Gets the system prompt based on the profession of the AI. -
 * showTextField(): Shows the text field and submit button. - cleanUpThreads(): Cleans up and
 * cancels all active threads.
 */
public class GuessingController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private Rectangle chef;
  @FXML private Rectangle cleaner;
  @FXML private Rectangle daughter;
  @FXML private Button btnSubmit;
  @FXML private TextField txtInput;
  @FXML private Label lbTimesUp;
  @FXML private Rectangle rectangleBackground;
  @FXML private Button btnResults;
  @FXML private Label lbTimer;
  @FXML private ImageView circleChef;
  @FXML private ImageView circleCleaner;
  @FXML private ImageView circleDaughter;
  @FXML private Label lbExplain;
  @FXML private ImageView loadingIndicator;
  private TranslateTransition translateTransition;

  private String chosenSuspect;
  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private List<Thread> threads = new ArrayList<Thread>();
  private TimerUtility timer;
  private Timeline guessingTimerCheckTimeline;

  @FXML
  private void initialize() {

    loadingIndicator.setVisible(false); // Hide loading indicator initially
    loadingIndicator.setImage(new Image(getClass().getResourceAsStream("/images/necklace.png")));
    translateTransition = new TranslateTransition(Duration.seconds(2), loadingIndicator);
    translateTransition.setFromX(-50); // Start position (off-screen)
    translateTransition.setToX(720); // End position (adjust as needed)
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE); // Loop the animation
    translateTransition.setAutoReverse(true); // Move back and forth

    // set text field and labels to disabled and invisible
    txtInput.setDisable(true);
    btnSubmit.setDisable(true);
    txtInput.setVisible(false);
    btnSubmit.setVisible(false);
    lbExplain.setVisible(false);

    // times up screen
    rectangleBackground.setVisible(false);
    lbTimesUp.setDisable(true);
    lbTimesUp.setVisible(false);
    btnResults.setDisable(true);
    btnResults.setVisible(false);

    // select indicators
    circleChef.setVisible(false);
    circleCleaner.setVisible(false);
    circleDaughter.setVisible(false);

    setProfession("AI");

    timer = new TimerUtility(60, lbTimer);
    timer.start();
    timeUpCheck();

    txtInput.setOnKeyPressed(
        event -> {
          switch (event.getCode()) {
            case ENTER:
              btnSubmit.fire(); // Trigger the send button programmatically
              break;
            default:
              break;
          }
        });
  }

  public void enableGameOver() {
    lbTimesUp.setDisable(false);
    btnResults.setDisable(false);
    lbTimesUp.setVisible(true);
    btnResults.setVisible(true);
  }

  private void timeUpCheck() {
    guessingTimerCheckTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (timer != null && timer.isFinished()) {
                    // Handle the case when the timer has finished
                    System.out.println("Guessing timer has finished.");
                    // You might want to perform specific actions or show a notification
                    try {
                      App.openGameLost();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                    guessingTimerCheckTimeline.stop();
                  }
                }));
    guessingTimerCheckTimeline.setCycleCount(Timeline.INDEFINITE);
    guessingTimerCheckTimeline.play();
  }

  /**
   * This method is called when the user clicks on the chef rectangle. It sets the chosen suspect to
   * chef and updates the selected suspect label. This method is called when the user clicks on the
   * chef rectangle. It sets the chosen suspect to chef and updates the selected suspect label.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickedChef(MouseEvent event) {
    chosenSuspect = "the chef James";
    showTextField();
    circleChef.setVisible(true);
    circleCleaner.setVisible(false);
    circleDaughter.setVisible(false);
  }

  /**
   * This method is called when the user clicks on the cleaner rectangle. It sets the chosen suspect
   * to cleaner and updates the selected suspect label. This method is called when the user clicks
   * on the cleaner rectangle. It sets the chosen suspect to cleaner and updates the selected
   * suspect label.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickedCleaner(MouseEvent event) {
    chosenSuspect = "the cleaner Alex";
    showTextField();
    circleChef.setVisible(false);
    circleCleaner.setVisible(true);
    circleDaughter.setVisible(false);
  }

  /**
   * This method is called when the user clicks on the daughter rectangle. It sets the chosen
   * suspect to daughter and updates the selected suspect label. This method is called when the user
   * clicks on the daughter rectangle. It sets the chosen suspect to daughter and updates the
   * selected suspect label.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickedDaughter(MouseEvent event) {
    chosenSuspect = "the daughter Maria";
    showTextField();
    circleChef.setVisible(false);
    circleCleaner.setVisible(false);
    circleDaughter.setVisible(true);
  }

  /**
   * This method is called when the user clicks the submit button. It will submit the user's guess
   * and run the AI chat operation. This method is called when the user clicks the submit button. It
   * will submit the user's guess and run the AI chat operation.
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
    ChatMessage userMessage =
        new ChatMessage("user", "SELECTED USER: " + chosenSuspect + "USER MESSAGE: " + message);

    loadingIndicator.setVisible(true);
    translateTransition.play(); // Start the animation

    // Run the AI chat operation in a background thread
    Task<Void> task =
        new Task<Void>() {
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
                    loadingIndicator.setVisible(false); // Hide loading indicator after response
                    translateTransition.stop(); // Stop the animation
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
   * This method is called when the user clicks the see results button. This button is presented
   * when the user runs out of time.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickSeeResults(ActionEvent event) {
    try {
      App.playSound("button.mp3");
      cleanUpThreads();
      App.openGameOver(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method sets the profession of the AI. It will set the system prompt based on the
   * profession.
   *
   * @param profession the profession of the AI
   */
  public void setProfession(String profession) {
    this.profession = profession;

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.5)
              .setMaxTokens(200);

      // run chat operation in a background thread
      Task<Void> task =
          new Task<Void>() {
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

  /** This method shows the text field and submit button. */
  private void showTextField() {
    txtInput.setDisable(false);
    txtInput.setVisible(true);
    btnSubmit.setDisable(false);
    btnSubmit.setVisible(true);
    lbExplain.setVisible(true);
  }

  /** This method cleans up and cancels all active threads. */
  private void cleanUpThreads() {
    timer.reset();
    if (!threads.isEmpty()) {
      for (Thread thread : threads) {
        thread.interrupt();
      }
      threads.clear();
    }
  }
}
