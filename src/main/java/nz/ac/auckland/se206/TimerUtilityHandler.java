package nz.ac.auckland.se206;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

/**
 * Utility class to handle the timer for the game.
 */
public class TimerUtilityHandler {

  /** 
  * Method to set the timer for the game.
  *
  * @param timer The timer object
  * @param timerLabel The label to display the timer
  **/
  public static void setTimer(TimerUtility timer, Label timerLabel) {
    // Add a listener to the timer to update the timer label
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });

    // Create an animation timer to update the timer label
    AnimationTimer timerAnimation =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            timerLabel.setText(timer.formatTime(timer.getSecondsLeft()));
          }
        };
        
    // Start the timer animation to update the timer label
    timerAnimation.start();
  }
}
