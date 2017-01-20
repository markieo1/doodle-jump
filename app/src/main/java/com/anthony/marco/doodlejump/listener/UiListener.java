package com.anthony.marco.doodlejump.listener;

/**
 * Created by marco on 20-1-2017.
 */

public interface UiListener {
    /**
     * Goes to the main menu
     */
    void onMainMenu();

    /**
     * Starts the game
     */
    void onStartGame();

    /**
     * Stops the entire game and goes into the game over callback.
     */
    void onStopGame();

    /**
     * Shows the scoreboard
     */
    void onShowScoreboard();
}
