package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerUtility {
  private Timeline timeline;
  private IntegerProperty timeSeconds;
  private int duration;
  public Label timerLabel;

  public TimerUtility(int duration, Label timerLabel) {
    this.duration = duration;
    this.timeSeconds = new SimpleIntegerProperty(duration);
    this.timerLabel = timerLabel;
    this.timerLabel.setText(formatTime(duration));

    timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);

    KeyFrame keyFrame =
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              int currentTime = timeSeconds.get();
              if (currentTime > 0) {
                timeSeconds.set(currentTime - 1);
                timerLabel.setText(formatTime(currentTime - 1));
              } else {
                timeline.stop();
                timerLabel.setText("Time's up!");
              }
            });

    timeline.getKeyFrames().add(keyFrame);
  }

  public String formatTime(int totalSeconds) {
    int minutes = totalSeconds / 60;
    int seconds = totalSeconds % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  public void start() {
    timeline.playFromStart();
  }

  public void pause() {
    timeline.pause();
  }

  public void reset() {
    timeline.stop();
    timeSeconds.set(duration);
    timerLabel.setText(formatTime(duration));
  }

  public IntegerProperty timeSecondsProperty() {
    return timeSeconds;
  }

  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }
}
