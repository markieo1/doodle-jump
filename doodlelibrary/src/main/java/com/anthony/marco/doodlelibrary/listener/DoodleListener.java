package com.anthony.marco.doodlelibrary.listener;

/**
 * Created by marco on 6-1-2017.
 */

public interface DoodleListener {
    /**
     * Occurs when the game has ended or is game over.
     *
     * @param score The final score
     */
    void gameOver(int score);

    /**
     * Occurs when the score of the player has changed
     *
     * @param newScore The new score
     */
    void scoreChanged(int newScore);
    void updateTimer(long timeLeft);
}
