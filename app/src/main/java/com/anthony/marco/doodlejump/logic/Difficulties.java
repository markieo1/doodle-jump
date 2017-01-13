package com.anthony.marco.doodlejump.logic;

/**
 * Created by antho on 12-1-2017.
 */

/**
 * Determines the Y-as in difficulties
 */
public enum Difficulties {
    VERY_EASY(20000),
    EASY(40000),
    NORMAL(60000),
    HARD(80000),
    VERY_HARD(100000);


    private final int difficultLevel;

    Difficulties(int difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

    public int getValue() {
        return difficultLevel;
    }

    /**
     * Gets the current difficulty depending on the YAS
     * @param YAS needs a positive YAS
     * @return
     */
    public static Difficulties getDifficulty(float YAS){
        Difficulties difficulty;

        if (YAS <= VERY_EASY.getValue()){
            difficulty = VERY_EASY;
        }
        else if(YAS >= VERY_EASY.getValue() && YAS <= EASY.getValue()){
            difficulty = EASY;
        }
        else if(YAS >= EASY.getValue() && YAS <= NORMAL.getValue()){
            difficulty = NORMAL;
        }
        else if(YAS >= NORMAL.getValue() && YAS <= HARD.getValue()){
            difficulty = HARD;
        }
        else if(YAS >= HARD.getValue() && YAS <= VERY_HARD.getValue()){
            difficulty = VERY_HARD;
        }
        else{
            difficulty = VERY_HARD;
        }

        return difficulty;
    }

}
