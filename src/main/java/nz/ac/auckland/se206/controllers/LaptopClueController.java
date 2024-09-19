package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// Controller class for the Laptop Clue scene
public class LaptopClueController {
  @FXML private Button backButton; // Button to go back to the previous scene
  @FXML private Circle alexCircle; // Circle representing Alex
  @FXML private Circle mariaCircle; // Circle representing Maria
  @FXML private Circle jamesCircle; // Circle representing James
  @FXML private Label timerLabel; // Label to display the timer

  // Method to set the timer and update the timer label
  public void setTimer(TimerUtility timer) {
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

    // AnimationTimer to continuously update the timer label
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

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Method called when the controller is initialized
  @FXML
  public void initialize() {
    // Set images for the circles
    jamesCircle.setFill(new ImagePattern(new Image("/images/chef.jpg")));
    mariaCircle.setFill(new ImagePattern(new Image("/images/daughter.jpg")));
    alexCircle.setFill(new ImagePattern(new Image("/images/cleaner.jpg")));
  }

  // Method called when a circle is clicked
  @FXML
  private void onCircleClicked(MouseEvent event) {
    try {
      if (event.getTarget() == jamesCircle) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/jamesClue.fxml");
      } else if (event.getTarget() == mariaCircle) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/mariaClue.fxml");
      } else if (event.getTarget() == alexCircle) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/alexClue.fxml");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method called when the back button is clicked
  @FXML
  private void onGoBack(ActionEvent event) {
    try {
      // Go back to the crime scene
      App.playSound("button.mp3");
      App.closeClue(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
