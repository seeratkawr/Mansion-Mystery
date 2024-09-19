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
  private Label timerLabel;

  // Constructor to initialize the timer with a specific duration and label
  public TimerUtility(int duration, Label timerLabel) {
    this.duration = duration;
    this.timeSeconds = new SimpleIntegerProperty(duration);
    this.timerLabel = timerLabel;
    this.timerLabel.setText(formatTime(duration));

    // Initialize the timeline with indefinite cycle count
    timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);

    // Define the key frame to be executed every second
    KeyFrame keyFrame =
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              int currentTime = timeSeconds.get();
              if (currentTime > 0) {
                // Decrease the time and update the label
                timeSeconds.set(currentTime - 1);
                timerLabel.setText(formatTime(currentTime - 1));
              } else {
                // Stop the timeline and update the label when time is up
                timeline.stop();
                timerLabel.setText("Time's up!");
              }
            });

    timeline.getKeyFrames().add(keyFrame);
  }

  // Method to format the time in mm:ss format
  public String formatTime(int totalSeconds) {
    int minutes = totalSeconds / 60;
    int seconds = totalSeconds % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  // Method to start the timer
  public void start() {
    timeline.playFromStart();
  }

  // Method to pause the timer
  public void pause() {
    timeline.pause();
  }

  // Method to reset the timer to the initial duration
  public void reset() {
    timeline.stop();
    timeSeconds.set(duration);
    timerLabel.setText(formatTime(duration));
  }

  // Method to get the timeSeconds property
  public IntegerProperty timeSecondsProperty() {
    return timeSeconds;
  }

  // Method to set a new timer label
  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  // Method to check if the timer has finished
  public boolean isFinished() {
    return timeSeconds.get() == 0;
  }

  // Method to get the remaining seconds
  public int getSecondsLeft() {
    return timeSeconds.get();
  }
}
