package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

/**
 * The GuessingController class is responsible for handling the user interactions and game logic for
 * the guessing game. It manages the UI components, handles user input, and communicates with the AI
 * to process the user's guesses.
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

  private String profession;
  private List<Thread> threads = new ArrayList<Thread>();
  private TimerUtility timer;
  private Timeline guessingTimerCheckTimeline;

  @FXML
  private void initialize() {
    App.playSound("guessingAudio.mp3");
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

    App.setProfession("AI", null, null, null);
    profession = App.getCurrentProfession();

    // set up timer
    timer = new TimerUtility(60, lbTimer);
    timer.start();
    timeUpCheck();

    // Add event handler for the Enter key to send the message
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

  /** This method is called to enable game over mode */
  public void enableGameOver() {
    lbTimesUp.setDisable(false);
    btnResults.setDisable(false);
    lbTimesUp.setVisible(true);
    btnResults.setVisible(true);
  }

  /** This method is called to check the timer */
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
    // Set the cycle count for the timeline
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
    App.setChosenSuspect("the chef James");
    showTextField();
    // changing visibility of circles
    circleChef.setVisible(true);
    circleCleaner.setVisible(false);
    circleDaughter.setVisible(false);
    // disabling other rectangles
    cleaner.setDisable(true);
    daughter.setDisable(true);
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
    App.setChosenSuspect("the cleaner Alex");
    showTextField();
    // changing visibility of circles
    circleChef.setVisible(false);
    circleCleaner.setVisible(true);
    circleDaughter.setVisible(false);
    // disabling other rectangles
    chef.setDisable(true);
    daughter.setDisable(true);
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
    App.setChosenSuspect("the daughter Maria");
    showTextField();
    // changing visibility of circles
    circleChef.setVisible(false);
    circleCleaner.setVisible(false);
    circleDaughter.setVisible(true);
    // disabling other rectangles
    chef.setDisable(true);
    cleaner.setDisable(true);
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

    // clean up and cancel threads
    cleanUpThreads();
    System.out.println("Submit message clicked");
    lbTimer.setVisible(false);

    App.handleGpt(profession, txtInput, null, loadingIndicator, translateTransition, event);
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
      // Play button click sound and clean up any active threads
      App.playSound("button.mp3");
      cleanUpThreads();
      App.openGameOver(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
