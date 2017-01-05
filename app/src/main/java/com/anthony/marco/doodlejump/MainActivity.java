package com.anthony.marco.doodlejump;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private DoodleSurfaceView doodleSurfaceView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        mSensorManager = (SensorManager) App.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(doodleSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }
}
