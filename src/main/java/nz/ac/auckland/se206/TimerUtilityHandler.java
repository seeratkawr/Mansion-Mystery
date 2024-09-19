package nz.ac.auckland.se206;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class TimerUtilityHandler {

  public static void setTimer(TimerUtility timer, Label timerLabel) {
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

    timerAnimation.start();
  }
}
