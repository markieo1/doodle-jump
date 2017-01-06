package com.anthony.marco.doodlejump;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private DoodleSurfaceView doodleSurfaceView;

    private View gameButtonsView;
    private View mainMenuButtonsView;

    private Button startGameButton;
    private Button stopGameButton;

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
        mainMenuButtonsView.setVisibility(View.GONE);
        gameButtonsView.setVisibility(View.VISIBLE);

        doodleSurfaceView.startGame();
    }

    private void stopGame() {
        mSensorManager.unregisterListener(doodleSurfaceView);
        isGameStarted = false;

        mainMenuButtonsView.setVisibility(View.VISIBLE);
        gameButtonsView.setVisibility(View.GONE);

        doodleSurfaceView.stopGame();
    }
}
