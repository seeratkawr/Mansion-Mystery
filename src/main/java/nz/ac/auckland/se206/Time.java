package nz.ac.auckland.se206;

/** A class for a time object that can be incremented and decremented. */
public class Time {
    private int minutes;
    private int seconds;

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

    /** Increments the time by one second. */
    public void incrementTime() {
        seconds = seconds + 1;
        if (seconds == 60) {
            seconds = 0;
            minutes = minutes + 1;
        }
    }

    /** Decrements the time by one second. */
    public void decrementTime() {
        seconds = seconds - 1;
        if (seconds == -1) {
            seconds = 59;
            minutes = minutes - 1;
        }
    }

    /**
     * Returns the minutes of the time.
     *
     * @return the minutes of the time
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Returns the seconds of the time.
     *
     * @return the seconds of the time
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Returns the time as a string in the format "mm:ss".
     *
     * @return the time as a string
     */
    @Override
    public String toString() {
        String stringSeconds;
        if (seconds < 10) {
            stringSeconds = ("0" + seconds);
            return minutes + ":" + stringSeconds;
        } else {
            return minutes + ":" + seconds;
        }
    }
}
