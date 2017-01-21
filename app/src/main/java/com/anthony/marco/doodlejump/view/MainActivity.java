package com.anthony.marco.doodlejump.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.UiListener;
import com.anthony.marco.doodlejump.view.fragment.AttractFragment;
import com.anthony.marco.doodlejump.view.fragment.GameOverFragment;
import com.anthony.marco.doodlejump.view.fragment.GameOverlayFragment;
import com.anthony.marco.doodlejump.view.fragment.MainMenuFragment;
import com.anthony.marco.doodlejump.view.fragment.ScoreboardFragment;
import com.anthony.marco.doodlelibrary.listener.DoodleListener;
import com.anthony.marco.doodlelibrary.model.Score;
import com.anthony.marco.doodlelibrary.view.DoodleSurfaceView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity implements DoodleListener, UiListener {
    private final String TAG = "MainActivity";

    /**
     * The time (MILLISECONDS) before the game goes into attract mode
     */
    private static final long IDLE_TIME = 10000;

    private DoodleSurfaceView doodleSurfaceView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private UiState currentUiState;

    private Score currentScore;

    private Handler idleHandler;
    private Runnable idleRunnable;

    private Fragment currentFragment;

    private DatabaseReference mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUiState(UiState.MAIN_MENU);

        currentScore = new Score();
        scores = new ArrayList<>();
        scoresAdapter = new ScoresAdapter(getLayoutInflater(), scores);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> data = dataSnapshot.getChildren().iterator();

                if (data.hasNext()) {
                    scores.clear();
                }

                while (data.hasNext()) {
                    DataSnapshot currentData = data.next();

                    if (currentData == null)
                        continue;

                    Score score = currentData.getValue(Score.class);

                    if (score != null) {
                        scores.add(score);
                        scoresAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setUiState(UiState.MAIN_MENU);

        idleHandler = new Handler(Looper.getMainLooper());
        idleRunnable = new Runnable() {
            @Override
            public void run() {
                //handle your IDLE state
                goAttractMode();
            }
        };

        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        hideSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                hideSystemUI();
            }
        });

        // Add wake lock to prevent the screen from sleeping
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Load the sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onPause() {
        super.onPause();

        doodleSurfaceView.pause();

        idleHandler.removeCallbacks(idleRunnable);

        if (currentUiState == UiState.GAME)
            unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        doodleSurfaceView.resume();

        if (currentUiState == UiState.GAME)
            registerListeners();

        if (currentUiState == UiState.MAIN_MENU) {
            scheduleIdleCallback();
        }

        switchViews();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (currentUiState == UiState.MAIN_MENU) {
            scheduleIdleCallback();
        }
    }

    private void scheduleIdleCallback() {
        Log.i(TAG, "Starting delayed idle runnable.");
        idleHandler.removeCallbacks(idleRunnable);
        idleHandler.postDelayed(idleRunnable, IDLE_TIME);
    }

    /**
     * Registers the listeners for the sensors
     */
    private void registerListeners() {
        mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Unregisters the listeners for the sensors
     */
    private void unregisterListeners() {
        mSensorManager.unregisterListener(doodleSurfaceView);
    }

    /**
     * Switches to attract mode
     */
    private void goAttractMode() {
        Log.i(TAG, "Switching to attract mode!");
        setUiState(UiState.ATTRACT);
        switchViews();
    }

    /**
     * Hides the SystemUI
     */
    private void hideSystemUI() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * Changes the UiState
     *
     * @param uiState the new ui state
     */
    private void setUiState(UiState uiState) {
        this.currentUiState = uiState;
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.VALUE, uiState.ordinal());
        mFirebaseAnalytics.logEvent("set_ui_state", bundle);
        Log.i(TAG, "UI State changed, new = " + uiState);
    }

    /**
     * Switches the visibility off the views according to the Ui State
     */
    private void switchViews() {
        if (currentUiState != UiState.MAIN_MENU)
            idleHandler.removeCallbacks(idleRunnable);

        String tag;

        Bundle arguments = new Bundle();
        Fragment newFragment;

        switch (currentUiState) {
            case GAME: {
                newFragment = new GameOverlayFragment();
                tag = GameOverlayFragment.TAG;
                break;
            }
            case GAME_OVER: {
                newFragment = new GameOverFragment();
                arguments.putSerializable(GameOverFragment.ARG_FINAL_SCORE, currentScore);
                tag = GameOverFragment.TAG;
                break;
            }
            case SCOREBOARD: {
                newFragment = new ScoreboardFragment();
                tag = ScoreboardFragment.TAG;
                break;
            }
            case ATTRACT: {
                newFragment = new AttractFragment();
                tag = AttractFragment.TAG;
                break;
            }
            default:
            case MAIN_MENU: {
                newFragment = new MainMenuFragment();
                tag = MainMenuFragment.TAG;
                break;
            }
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        newFragment.setArguments(arguments);
        transaction.replace(R.id.activity_main, newFragment, tag);
        transaction.commit();

        currentFragment = newFragment;
    }    
        
    /**
     * Saves the score in the database
     *
     * @param score The score to save
     */
    private void saveScore(Score score) {
        mDatabase.child("scores").push().setValue(score);


        // Save the score in the database
        /*SaveScoreTask saveScoreTask = new SaveScoreTask(this, this);
        saveScoreTask.execute(score);*/
    }

    @Override
    public void gameOver(final int score) {
        // Run On UI Thread since this callback is called from another thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScore.setScore(score);

                Bundle bundle = new Bundle();
                bundle.putInt(FirebaseAnalytics.Param.SCORE, score);
                mFirebaseAnalytics.logEvent("game_over", bundle);

                saveScore(currentScore);

                unregisterListeners();
                setUiState(UiState.GAME_OVER);
                switchViews();
            }
        });
    }

    @Override
    public void scoreChanged(final int newScore) {
        // Run On UI Thread since this callback is called from another thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScore.setScore(newScore);
                // Update the score label
                if (currentFragment instanceof GameOverlayFragment)
                    ((GameOverlayFragment) currentFragment).setScore(newScore);
            }
        });
    }

    @Override
    public void updateTimer(final long timeLeft) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentFragment instanceof GameOverlayFragment)
                    ((GameOverlayFragment) currentFragment).setRemainingTime(timeLeft);
            }
        });
    }

    @Override
    public void onMainMenu() {
        setUiState(UiState.MAIN_MENU);
        switchViews();

        scheduleIdleCallback();
    }

    @Override
    public void onStartGame() {
        setUiState(UiState.GAME);

        registerListeners();

        switchViews();

        doodleSurfaceView.startGame(this);
    }

    @Override
    public void onStopGame() {
        unregisterListeners();
        doodleSurfaceView.stopGame();
    }

    @Override
    public void onShowScoreboard() {
        setUiState(UiState.SCOREBOARD);
        switchViews();
    }
}
