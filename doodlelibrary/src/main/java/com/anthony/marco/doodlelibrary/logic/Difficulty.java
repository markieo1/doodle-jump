package com.anthony.marco.doodlelibrary.logic;

/**
 * Created by antho on 12-1-2017.
 */

/**
 * Determines the Y-as in difficulties
 */
public enum Difficulty {
    VERY_EASY(20000),
    EASY(40000),
    NORMAL(60000),
    HARD(80000),
    VERY_HARD(100000);


    private final int difficultLevel;

    Difficulty(int difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

    public int getValue() {
        return difficultLevel;
    }

    /**
     * Gets the current difficulty depending on the YAS
     *
     * @param YAS needs a positive YAS
     * @return
     */
    public static Difficulty getDifficulty(float YAS) {
        Difficulty difficulty;

        if (YAS <= VERY_EASY.getValue()) {
            difficulty = VERY_EASY;
        } else if (YAS >= VERY_EASY.getValue() && YAS <= EASY.getValue()) {
            difficulty = EASY;
        } else if (YAS >= EASY.getValue() && YAS <= NORMAL.getValue()) {
            difficulty = NORMAL;
        } else if (YAS >= NORMAL.getValue() && YAS <= HARD.getValue()) {
            difficulty = HARD;
        } else if (YAS >= HARD.getValue() && YAS <= VERY_HARD.getValue()) {
            difficulty = VERY_HARD;
        } else {
            difficulty = VERY_HARD;
        }

        return difficulty;
    }

}
