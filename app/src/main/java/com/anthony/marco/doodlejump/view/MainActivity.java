package com.anthony.marco.doodlejump.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.anthony.marco.doodlejump.App;
import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.DoodleListener;

public class MainActivity extends Activity implements DoodleListener {
    private final String TAG = "MainActivity";

    private DoodleSurfaceView doodleSurfaceView;

    private View gameButtonsView;
    private View mainMenuButtonsView;
    private View gameOverView;

    private TextView scoreTextView;
    private TextView finalScoreTextView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private UiState currentUiState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUiState(UiState.MAIN_MENU);

        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        gameButtonsView = findViewById(R.id.game_buttons);
        mainMenuButtonsView = findViewById(R.id.main_menu_buttons);
        gameOverView = findViewById(R.id.game_over_layout);

        scoreTextView = (TextView) gameButtonsView.findViewById(R.id.score_text_view);
        finalScoreTextView = (TextView) gameOverView.findViewById(R.id.final_score_text_view);

        Button startGameButton = (Button) mainMenuButtonsView.findViewById(R.id.start_game_button);
        Button aboutUsButton = (Button) mainMenuButtonsView.findViewById(R.id.about_us_button);
        Button playAgainButton = (Button) gameOverView.findViewById(R.id.play_again_button);
        Button stopGameButton = (Button) gameButtonsView.findViewById(R.id.stop_game_button);

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
        mSensorManager = (SensorManager) App.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Register all the button listeners.
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AboutUsActivity.class);

                // Start the intent
                startActivity(intent);
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        stopGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopGame();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (currentUiState == UiState.GAME)
            unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentUiState == UiState.GAME)
            registerListeners();

        switchViews();
    }

    /**
     * Starts the game
     */
    private void startGame() {
        setUiState(UiState.GAME);

        registerListeners();

        switchViews();

        doodleSurfaceView.startGame(this);
    }

    /**
     * Stops the entire game and goes into the game over callback.
     */
    private void stopGame() {
        unregisterListeners();
        doodleSurfaceView.stopGame();
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
     * Switches the visibility off the views according to the Ui State
     */
    private void switchViews() {
        switch (currentUiState) {
            case GAME: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.VISIBLE);
                gameOverView.setVisibility(View.GONE);
                break;
            }
            case MAIN_MENU: {
                mainMenuButtonsView.setVisibility(View.VISIBLE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                break;
            }
            case GAME_OVER: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void gameOver(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                unregisterListeners();
                setUiState(UiState.GAME_OVER);
                switchViews();

                finalScoreTextView.setText(String.valueOf(score));
            }
        });
    }

    @Override
    public void scoreChanged(final int newScore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update the score label
                scoreTextView.setText(String.valueOf(newScore));
            }
        });
    }

    /**
     * Changes the UiState
     *
     * @param uiState the new ui state
     */
    private void setUiState(UiState uiState) {
        this.currentUiState = uiState;
        Log.i(TAG, "UI State changed, new = " + uiState);
    }
}
