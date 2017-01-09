package com.anthony.marco.doodlejump.view;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anthony.marco.doodlejump.App;
import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.DoodleListener;

public class MainActivity extends Activity implements DoodleListener {

    private DoodleSurfaceView doodleSurfaceView;

    private View gameButtonsView;
    private View mainMenuButtonsView;

    private Button startGameButton;
    private Button stopGameButton;
    private TextView scoreTextView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private boolean isGameStarted;

    public MainActivity() {
        isGameStarted = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        gameButtonsView = findViewById(R.id.game_buttons);
        mainMenuButtonsView = findViewById(R.id.main_menu_buttons);

        startGameButton = (Button) mainMenuButtonsView.findViewById(R.id.start_game_button);
        stopGameButton = (Button) gameButtonsView.findViewById(R.id.stop_game_button);
        scoreTextView = (TextView) gameButtonsView.findViewById(R.id.score_text_view);

        startGameButton.setOnClickListener(new View.OnClickListener() {
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

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager) App.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isGameStarted)
            mSensorManager.unregisterListener(doodleSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isGameStarted) {
            mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);
            mainMenuButtonsView.setVisibility(View.GONE);
            gameButtonsView.setVisibility(View.VISIBLE);
        }
    }

    private void startGame() {
        isGameStarted = true;

        // Register the sensor listener
        mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);

        // Hide the buttons
        hideViews();

        doodleSurfaceView.startGame(this);
    }

    private void stopGame() {
        mSensorManager.unregisterListener(doodleSurfaceView);
        isGameStarted = false;

        hideViews();

        doodleSurfaceView.stopGame();
    }

    private void hideViews() {
        mainMenuButtonsView.setVisibility(isGameStarted ? View.GONE : View.VISIBLE);
        gameButtonsView.setVisibility(isGameStarted ? View.VISIBLE : View.GONE);
    }

    @Override
    public void gameOver(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSensorManager.unregisterListener(doodleSurfaceView);
                isGameStarted = false;
                hideViews();

                Toast.makeText(getApplicationContext(), "Score was: " + score, Toast.LENGTH_LONG).show();
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
}
