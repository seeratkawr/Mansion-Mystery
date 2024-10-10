package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * The TimerUtility class is responsible for managing the timer in the application. It initializes the
 * timer with a specified duration and updates the timer label every second. The timer can be started,
 * paused, and reset. The class also provides methods to check if the timer has finished, get the time
 * remaining in seconds, and format the time in minutes and seconds.
 */
public class TimerUtility {
  private Timeline timeline;
  private IntegerProperty timeSeconds; 
  private int duration; 
  private Label timerLabel;

  /**
   * Constructor for the TimerUtility class.
   * 
   * @param duration
   * @param timerLabel
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
   * Method to format the time in minutes and seconds.
   * 
   * @param totalSeconds
   * @return
   */
  public String formatTime(int totalSeconds) {
    int minutes = totalSeconds / 60;
    int seconds = totalSeconds % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  /**
   * Start the timer from the beginning of the duration.
   */
  public void start() {
    timeline.playFromStart();
  }

  /**
   * Pause the timer at the current time remaining value.
   */
  public void pause() {
    timeline.pause();
  }

  /**
   * Stop the timer and reset the time to the initial duration.
   */
  public void reset() {
    timeline.stop();
    timeSeconds.set(duration);
    timerLabel.setText(formatTime(duration));
  }

  /**
   * Getter method for the timeSeconds property.
   * 
   * @return The timeSeconds property
   */
  public IntegerProperty timeSecondsProperty() {
    return timeSeconds;
  }

  /**
   * Getter method for the timer label.
   * 
   * @return The timer label
   */
  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  /**
   * Method to check if the timer has finished.
   * 
   * @return True if the timer has finished, false otherwise
   */
  public boolean isFinished() {
    return timeSeconds.get() == 0;
  }

  /**
   * Method to get the time remaining in seconds.
   * 
   * @return The time remaining in seconds
   */
  public int getSecondsLeft() {
    return timeSeconds.get();
  }
}
