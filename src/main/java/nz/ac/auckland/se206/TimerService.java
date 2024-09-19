package nz.ac.auckland.se206;

import javafx.scene.control.Label;

public class TimerService {
  private static TimerUtility timerUtility;

  public static TimerUtility getTimer(Label timerLabel, int initialSeconds) {
      if (timerUtility == null) {
          // You can initialize the timer with an appropriate duration
          timerUtility = new TimerUtility(initialSeconds, timerLabel);
      }
      return timerUtility;
  }

  public static void setTimer(TimerUtility timer) {
      timerUtility = timer;
  }
}
