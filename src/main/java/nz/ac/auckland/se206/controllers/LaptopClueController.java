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

// nz.ac.auckland.se206.controllers.LaptopClueController
public class LaptopClueController {
  @FXML private Button backButton;
  @FXML private Circle alexCircle;
  @FXML private Circle mariaCircle;
  @FXML private Circle jamesCircle;
  @FXML private Label timerLabel;

  private TimerUtility timer;

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
  public void initialize() {
    jamesCircle.setFill(new ImagePattern(new Image("/images/chef.jpg")));
    mariaCircle.setFill(new ImagePattern(new Image("/images/daughter.jpg")));
    alexCircle.setFill(new ImagePattern(new Image("/images/cleaner.jpg")));
  }

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
