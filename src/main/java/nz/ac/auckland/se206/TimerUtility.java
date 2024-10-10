package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * The TimerUtility class is responsible for managing the timer in the application. It initializes
 * the timer with a specified duration and updates the timer label every second. The timer can be
 * started, paused, and reset. The class also provides methods to check if the timer has finished,
 * get the time remaining in seconds, and format the time in minutes and seconds.
 */
public class TimerUtility {

  private Timeline timeline;
  private IntegerProperty timeSeconds;
  private int duration;
  private Label timerLabel;

  /**
   * Constructor for the TimerUtility class. Initializes the timer with the given duration and binds
   * the timer label.
   *
   * @param duration the duration of the timer in seconds
   * @param timerLabel the label that displays the remaining time
   */
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

  /**
   * Formats the time into minutes and seconds.
   *
   * @param totalSeconds the total time in seconds
   * @return the formatted time as a string in "MM:SS" format
   */
  public String formatTime(int totalSeconds) {
    int minutes = totalSeconds / 60;
    int seconds = totalSeconds % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  /** Starts the timer from the beginning of the specified duration. */
  public void start() {
    timeline.playFromStart();
  }

  /** Pauses the timer at the current remaining time. */
  public void pause() {
    timeline.pause();
  }

  /** Stops the timer and resets it to the initial duration. */
  public void reset() {
    timeline.stop();
    timeSeconds.set(duration);
    timerLabel.setText(formatTime(duration));
  }

  /**
   * Returns the time remaining property, which is an IntegerProperty.
   *
   * @return the timeSeconds property representing the remaining time in seconds
   */
  public IntegerProperty timeSecondsProperty() {
    return timeSeconds;
  }

  /**
   * Updates the timer label with a new label.
   *
   * @param timerLabel the new label to display the remaining time
   */
  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  /**
   * Checks if the timer has finished.
   *
   * @return true if the timer has finished (time is zero), false otherwise
   */
  public boolean isFinished() {
    return timeSeconds.get() == 0;
  }

  /**
   * Gets the time remaining in seconds.
   *
   * @return the remaining time in seconds
   */
  public int getSecondsLeft() {
    return timeSeconds.get();
  }
}
