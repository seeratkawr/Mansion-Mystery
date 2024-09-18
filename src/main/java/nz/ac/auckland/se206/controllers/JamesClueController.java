package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

// nz.ac.auckland.se206.controllers.JamesClueController

public class JamesClueController {
  @FXML private Rectangle rectangleButtons;
  @FXML private Label timerLabel;

  private TimerUtility timer;

  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    App.playSound("mouseclick.mp3");
    App.openLaptop(event);
  }

  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });
  }

  public Label getTimerLabel() {
    return timerLabel;
  }
}
