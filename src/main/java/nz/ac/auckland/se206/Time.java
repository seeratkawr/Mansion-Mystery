package nz.ac.auckland.se206;

/**
 * A class for a time object that can be incremented and decremented.
 */
public class Time {
  private int minutes; // Stores the minutes part of the time
  private int seconds; // Stores the seconds part of the time

  /**
   * Constructs a Time object with the given minutes and seconds.
   *
   * @param minutes the minutes of the time
   * @param seconds the seconds of the time
   */
  public Time(int minutes, int seconds) {
    this.minutes = minutes;
    this.seconds = seconds;
  }

  /**
   * Increments the time by one second.
   */
  public void incrementTime() {
    seconds = seconds + 1; // Increment seconds by 1
    if (seconds == 60) { // If seconds reach 60, reset to 0 and increment minutes
      seconds = 0;
      minutes = minutes + 1;
    }
  }

  /**
   * Decrements the time by one second.
   */
  public void decrementTime() {
    seconds = seconds - 1; // Decrement seconds by 1
    if (seconds == -1) { // If seconds go below 0, set to 59 and decrement minutes
      seconds = 59;
      minutes = minutes - 1;
    }
  }

  /**
   * Returns the minutes of the time as an integer.
   *
   * @return the minutes of the time
   */
  public int getMinutes() {
    return minutes; // Return the minutes part of the time
  }

  /**
   * Returns the seconds of the time as an integer.
   *
   * @return the seconds of the time
   */
  public int getSeconds() {
    return seconds; // Return the seconds part of the time
  }

  /**
   * Returns the time as a string in the format "mm:ss".
   *
   * @return the time as a string
   */
  @Override
  public String toString() {
    String stringSeconds;
    if (seconds < 10) { // If seconds are less than 10, add a leading zero
      stringSeconds = ("0" + seconds);
      return minutes + ":" + stringSeconds; // Return time in "mm:ss" format
    } else {
      return minutes + ":" + seconds; // Return time in "mm:ss" format
    }
  }
}
