package com.anthony.marco.doodlejump.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.anthony.marco.doodlejump.database.ScoreDatabaseHelper;
import com.anthony.marco.doodlejump.listener.DatabaseListener;
import com.anthony.marco.doodlelibrary.model.Score;

/**
 * Created by marco on 10-1-2017.
 */

public class NameInUseTask extends AsyncTask<String, Void, Boolean> {
    private ScoreDatabaseHelper mScoreDatabaseHelper;
    private DatabaseListener mDatabaseListener;
    private String nameToCheck;

    public NameInUseTask(Context context, DatabaseListener databaseListener) {
        this.mScoreDatabaseHelper = new ScoreDatabaseHelper(context);
        this.mDatabaseListener = databaseListener;
    }

    @Override
    protected Boolean doInBackground(String... names) {
        if (names == null || names.length == 0)
            return null;

        this.nameToCheck = names[0];

        return this.mScoreDatabaseHelper.isNameInUse(nameToCheck);
    }

    @Override
    protected void onPostExecute(Boolean inUse) {
        if (mDatabaseListener == null)
            return;

        mDatabaseListener.nameInUseChecked(nameToCheck, inUse);
    }
}
