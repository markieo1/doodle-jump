package com.anthony.marco.doodlejump.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.anthony.marco.doodlejump.model.Score;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by marco on 10-1-2017.
 */

public class ScoreDatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = "ScoreDatabaseHelper";
    private static final String DATABASE_NAME = "DoodleJumpDb";
    private static final int DB_VERSION = 1;

    private final String TBL_SCORES = "scores";
    private final String SCORES_COL_NAME = "name";
    private final String SCORES_COL_SCORE = "score";
    private final String SCORES_COL_DATE = "date";

    private final String DB_CREATE_SCORE = "CREATE TABLE `" + TBL_SCORES + "`" +
            "(`" + BaseColumns._ID + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            " `" + SCORES_COL_NAME + "` TEXT NOT NULL UNIQUE," +
            " `" + SCORES_COL_SCORE + "` INTEGER NOT NULL," +
            " `" + SCORES_COL_DATE + "` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP);";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating SQlite DB");
        sqLiteDatabase.execSQL(DB_CREATE_SCORE);
        Log.i(TAG, "SQlite DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    /**
     * Loads the scores from the database
     *
     * @param onlyLatestTen Determines if only the 10 latest score should be loaded
     * @return List containing the scores
     */
    public ArrayList<Score> loadScores(boolean onlyLatestTen) {
        Log.i(TAG, "Loading scores, onlyLatestTen = " + onlyLatestTen);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());

        ArrayList<Score> scores = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TBL_SCORES, new String[]{BaseColumns._ID, SCORES_COL_NAME, SCORES_COL_SCORE, SCORES_COL_DATE}, null, null, null, null, SCORES_COL_DATE + " DESC", onlyLatestTen ? "10" : null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String name = cursor.getString(cursor.getColumnIndex(SCORES_COL_NAME));
            int score = cursor.getInt(cursor.getColumnIndex(SCORES_COL_SCORE));

            Score scoreObj = new Score(id, name, score);

            String dateString = cursor.getString(cursor.getColumnIndex(SCORES_COL_DATE));
            try {
                Date date = dateFormat.parse(dateString);
                scoreObj.setDate(date);
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing id = " + id + " ,date = " + dateString, e);
            }

            scores.add(scoreObj);
        }

        cursor.close();
        db.close();
        Log.i(TAG, "Scores loaded, size = " + scores.size());
        return null;
    }

    /**
     * Saves a score in the database
     *
     * @param score The score to save
     */
    public long saveScore(Score score) {
        Log.i(TAG, "Starting save score, name = " + score.getName() + ", score = " + score.getScore());
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCORES_COL_NAME, score.getName());
        contentValues.put(SCORES_COL_SCORE, score.getScore());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TBL_SCORES, null, contentValues);

        db.close();

        Log.i(TAG, "Score saved, rowId = " + id);

        return id;
    }

    /**
     * Checks the Sqlite DB if the specified name is already used
     *
     * @param name The name to check
     * @return True if the the name is in use, else false.
     */
    public boolean isNameInUse(String name) {
        boolean isInUse = false;

        String selectQuery = "SELECT 1 FROM " + TBL_SCORES + " WHERE " + SCORES_COL_NAME + " = ?";

        Log.i(TAG, "Checking if name is in use, name = " + name);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});

        if (cursor.getCount() >= 1) {
            isInUse = true;
        }

        cursor.close();
        db.close();

        Log.i(TAG, "Name in use = " + isInUse);

        return isInUse;
    }
}
