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
    private Context mContext;
    private ScoreDatabaseHelper mScoreDatabaseHelper;
    private DatabaseListener mDatabaseListener;

    public SaveScoreTask(Context context, DatabaseListener databaseListener) {
        this.mContext = context;
        this.mScoreDatabaseHelper = new ScoreDatabaseHelper(context);
        this.mDatabaseListener = databaseListener;
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

    @Override
    protected void onPostExecute(Void aVoid) {
        // We load the newly saved scores.
        LoadScoresTask loadScoresTask = new LoadScoresTask(mContext, mDatabaseListener);
        loadScoresTask.execute(true);
    }
}
