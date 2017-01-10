package com.anthony.marco.doodlejump.listener;

import com.anthony.marco.doodlejump.model.Score;

import java.util.ArrayList;

/**
 * Created by marco on 10-1-2017.
 */

public interface DatabaseListener {
    /**
     * Callback used when the scores are loaded from the database
     *
     * @param scores
     */
    void scoresLoaded(ArrayList<Score> scores);

    /**
     * Callback used when the name in use has been checked
     *
     * @param name  The name to check
     * @param inUse The result if the name is already in use
     */
    void nameInUseChecked(String name, boolean inUse);
}
