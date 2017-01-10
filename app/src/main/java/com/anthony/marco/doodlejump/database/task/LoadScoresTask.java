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

public class LoadScoresTask extends AsyncTask<Boolean, Void, ArrayList<Score>> {
    private ScoreDatabaseHelper mScoreDatabaseHelper;
    private DatabaseListener mDatabaseListener;

    public LoadScoresTask(Context context, DatabaseListener databaseListener) {
        this.mScoreDatabaseHelper = new ScoreDatabaseHelper(context);
        this.mDatabaseListener = databaseListener;
    }

    @Override
    protected ArrayList<Score> doInBackground(Boolean... onlyLatestTen) {
        if (onlyLatestTen == null || onlyLatestTen.length == 0)
            return null;

        return this.mScoreDatabaseHelper.loadScores(onlyLatestTen[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Score> scores) {
        if (scores == null || mDatabaseListener == null)
            return;

        mDatabaseListener.scoresLoaded(scores);
    }
}
