package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/**
 * The GameOverController class is responsible for handling the game over screen in the application.
 * It initializes the game over screen, displays the result of the game, and manages the continue
 * button. The continue button is initially disabled and hidden, and it becomes enabled and visible
 * after a delay. The class also handles the action of clicking the continue button to restart the
 * game.
 */
public class GameOverController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private TextArea txtaResults;
  @FXML private Button continueButton;
  @FXML private ImageView selectedSuspect;
  @FXML private Label selectedSuspectName;

  private String result;
  private String chosenThief;

  /**
   * Initializes the controller class. This method is called after the FXML fields are injected.
   */
  @FXML
  private void initialize() {
    result = App.getAiGameResult();
    chosenThief = App.getChosenSuspect();

    typeOutMessage();

    // Disable the button initially
    continueButton.setDisable(true);
    continueButton.setVisible(false);

    // Set the image and name of the chosen suspect
    if (chosenThief.equals("the chef James")) {
      selectedSuspect.setImage(new Image("/images/chef.jpg"));
      selectedSuspectName.setText("The chef, James");
      // Set the image and name of the chosen suspect
    } else if (chosenThief.equals("the cleaner Alex")) {
      selectedSuspect.setImage(new Image("/images/cleaner.jpg"));
      selectedSuspectName.setText("The cleaner, Alex");
      // Set the image and name of the chosen suspect
    } else if (chosenThief.equals("the daughter Maria")) {
      selectedSuspect.setImage(new Image("/images/daughter.jpg"));
      selectedSuspectName.setText("The daughter, Maria");
    }

    // Enable the button after 10 seconds
    javafx.animation.PauseTransition pauseTransition =
        new PauseTransition(javafx.util.Duration.seconds(5));
    pauseTransition.setOnFinished(
        e -> {
          // Assuming there is a button to enable, replace 'yourButton' with the actual button
          // variable
          continueButton.setDisable(false);
          continueButton.setVisible(true);
        });
    pauseTransition.play();
  }

  /**
   * Restarts the game when the continue button is clicked.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void onClickedContinue(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    App.openGameLost();
  }

  /**
   * Types out the message in the text area character by character with a delay.
   */
  private void typeOutMessage() {
    final int[] currentIndex = {0};
    Timeline timeline = new Timeline();
    // Add a keyframe for each character to be typed
    KeyFrame keyFrame =
        new KeyFrame(
            Duration.millis(15),
            event -> {
              if (currentIndex[0] < result.length()) {
                txtaResults.appendText(String.valueOf(result.charAt(currentIndex[0])));
                currentIndex[0]++;
              }
            });

    // Set the number of keyframes to the length of the message
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(result.length()); // Run it for each character
    timeline.play();
  }
}
