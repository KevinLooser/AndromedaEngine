package engine;

public class Timer {

    private double lastLoopTime;

    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * Get the current system time, converted to seconds
     * @return current system time in seconds
     */
    public double getTime() {
        // get time in seconds
        return System.nanoTime() / 1000_000_000.0;
    }

    /**
     * Get the time that elapsed between the last time and now.
     * Update last time with current time.
     * @return time between the last time and now
     */
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}
