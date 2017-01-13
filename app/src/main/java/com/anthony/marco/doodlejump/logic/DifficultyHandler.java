package com.anthony.marco.doodlejump.logic;

/**
 * Created by antho on 12-1-2017.
 */

public class DifficultyHandler {
    /**
     * The platform width to begin with
     */
    private float initialPlatformWidth;

    /**
     * The minimal difference between two platforms
     */
    private float initialMinDifference;

    /**
     * The maximal difference between two platforms
     */
    private float initialMaxDifference;

    /**
     * The timer countdown to start with
     */
    private int initialCountDownTimerInS;

    /**
     * The last set difficulty
     */
    private Difficulties lastDifficulty;

    /**
     * The minimum amount of seconds that is allowed to countdown too
     */
    private static final int LOWEST_COUNT_DOWN_IN_SECONDS = 3;

    public void setInitialCountDownTimerInS(int initialCountDownTimerInS) {
        this.initialCountDownTimerInS = initialCountDownTimerInS;
    }

    /**
     * Set all the default values to start with
     * @param initialPlatformWidth The platform width to begin with
     * @param initialMinDifference The minimal difference between two platforms
     * @param initialMaxDifference The maximal difference between two platforms
     * @param initialCountDownTimerInS The timer countdown to start with
     */
    public DifficultyHandler(float initialPlatformWidth, float initialMinDifference, float initialMaxDifference, int initialCountDownTimerInS) {
        this.initialPlatformWidth = initialPlatformWidth;
        this.initialMinDifference = initialMinDifference;
        this.initialMaxDifference = initialMaxDifference;
        this.initialCountDownTimerInS = initialCountDownTimerInS;
        lastDifficulty = Difficulties.VERY_EASY;
    }

    public void setNewValues(float YAS){
        YAS *= -1;
        lastDifficulty = Difficulties.getDifficulty(YAS);

        switch (lastDifficulty){
            case EASY:
                initialMinDifference += 20;
                initialMaxDifference += 20;
                initialPlatformWidth -= 10;
                break;
            case NORMAL:
                initialMinDifference += 30;
                initialMaxDifference += 15;
                initialPlatformWidth -= 5;
                break;
            case HARD:
                initialMinDifference += 15;
                initialMaxDifference += 30;
                initialPlatformWidth -= 10;
                break;
            // When y is larger than VERY_HARD it stays that way
            default:
            case VERY_HARD:
                initialMinDifference += 20;
                initialMaxDifference += 15;
                initialPlatformWidth -= 5;
                break;
        }
        // We always want to decrease the timer by 1 but not when the difficulty is VERY_EASY. Also we don't want a lower timer than 3.
        if (!lastDifficulty.equals(Difficulties.VERY_EASY) && getInitialCountDownTimerInS() >= LOWEST_COUNT_DOWN_IN_SECONDS){
            setCountDownTimer(getInitialCountDownTimerInS() -1);
        }
    }

    public boolean needNewValues(float YAS){
        boolean needNewValues = false;

        // We want a positive int, since we are going up, we are going negative
        YAS *= -1;

        if (lastDifficulty.getValue() <= YAS){
            needNewValues = true;
        }

        return needNewValues;
    }

    public float getPlatformWidth() {
        return initialPlatformWidth;
    }

    private void setPlatformWidth(int initialPlatformWidth) {
        this.initialPlatformWidth = initialPlatformWidth;
    }

    public float getMinDifference() {
        return initialMinDifference;
    }

    private void setMinDifference(int initialMinDifference) {
        this.initialMinDifference = initialMinDifference;
    }

    public float getMaxDifference() {
        return initialMaxDifference;
    }

    private void setInitialMaxDifference(int initialMaxDifference) {
        this.initialMaxDifference = initialMaxDifference;
    }

    public int getInitialCountDownTimerInS() {
        return initialCountDownTimerInS;
    }

    private void setCountDownTimer(int initialCountDownTimerInS) {
        this.initialCountDownTimerInS = initialCountDownTimerInS;
    }
}
