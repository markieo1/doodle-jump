package com.anthony.marco.doodlejump.logic;

/**
 * Created by antho on 12-1-2017.
 */

public class DifficultyHandler {

    /**
     * The minimal difference between platforms for easy
     */
    private static final int MIN_DIFFERENCE_EASY = 20;

    /**
     * The maximal difference between platforms for easy
     */
    public static final int MAX_DIFFERENCE_EASY = 20;

    /**
     * The width to reduce the platform on easy
     */
    public static final int PLATFORM_WIDTH_EASY = 10;

    /**
     * The minimal difference between platforms for normal
     */
    public static final int MIN_DIFFERENCE_NORMAL = 30;

    /**
     * The maximal difference between platforms for normal
     */
    public static final int MAX_DIFFERENCE_NORMAL = 15;

    /**
     * The width to reduce the platform on normal
     */
    public static final int PLATFORM_WIDTH_NORMAL = 5;

    /**
     * The minimal difference between platforms for hard
     */
    public static final int MIN_DIFFERENCE_HARD = 15;

    /**
     * The maximal difference between platforms for hard
     */
    public static final int MAX_DIFFERENCE_HARD = 30;

    /**
     * The width to reduce the platform on hard
     */
    public static final int PLATFORM_WIDTH_HARD = 10;

    /**
     * The minimal difference between platforms for very hard
     */
    public static final int MIN_DIFFERENCE_VERY_HARD = 20;

    /**
     * The maximal difference between platforms for very hard
     */
    public static final int MAX_DIFFERENCE_VERY_HARD = 15;

    /**
     * The width to reduce the platform on very hard
     */
    public static final int PLATFORM_DIFFERENCE_VERY_HARD = 5;

    /**
     * The minimum amount of seconds that is allowed to countdown too
     */
    private static final int LOWEST_COUNT_DOWN_IN_SECONDS = 4;

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
                initialMinDifference += MIN_DIFFERENCE_EASY;
                initialMaxDifference += MAX_DIFFERENCE_EASY;
                initialPlatformWidth -= PLATFORM_WIDTH_EASY;
                break;
            case NORMAL:
                initialMinDifference += MIN_DIFFERENCE_NORMAL;
                initialMaxDifference += MAX_DIFFERENCE_NORMAL;
                initialPlatformWidth -= PLATFORM_WIDTH_NORMAL;
                break;
            case HARD:
                initialMinDifference += MIN_DIFFERENCE_HARD;
                initialMaxDifference += MAX_DIFFERENCE_HARD;
                initialPlatformWidth -= PLATFORM_WIDTH_HARD;
                break;
            // When y is larger than VERY_HARD it stays that way
            default:
            case VERY_HARD:
                initialMinDifference += MIN_DIFFERENCE_VERY_HARD;
                initialMaxDifference += MAX_DIFFERENCE_VERY_HARD;
                initialPlatformWidth -= PLATFORM_DIFFERENCE_VERY_HARD;
                break;
        }
        // We always want to decrease the timer by 1 but not when the difficulty is VERY_EASY. Also we don't want a lower timer than 4.
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
