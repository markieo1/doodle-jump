package com.anthony.marco.doodlejump.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.anthony.marco.doodlejump.database.ScoreDatabaseHelper;
import com.anthony.marco.doodlejump.listener.DatabaseListener;
import com.anthony.marco.doodlejump.model.Score;

import java.util.ArrayList;

/**
 * Created by marco on 10-1-2017.
 */

public class SaveScoreTask extends AsyncTask<Score, Void, Void> {
    private ScoreDatabaseHelper mScoreDatabaseHelper;

    public SaveScoreTask(Context context) {
        this.mScoreDatabaseHelper = new ScoreDatabaseHelper(context);
    }

    @Override
    protected Void doInBackground(Score... scores) {
        if (scores == null)
            return null;

        for (Score score : scores) {
            this.mScoreDatabaseHelper.saveScore(score);
        }

        return null;
    }
}
