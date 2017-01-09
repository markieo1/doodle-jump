package com.anthony.marco.doodlejump;

/**
 * Created by marco on 6-1-2017.
 */

public interface DoodleListener {
    void gameOver(int score);
    void scoreChanged(int newScore);
}
