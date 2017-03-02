package com.havermans.marco.gamelibrary;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.havermans.marco.gamelibrary.graphics.Overlay;
import com.havermans.marco.gamelibrary.logic.Scene;

/**
 * Created by marco on 27-2-2017.
 */

public class GameActivity extends Activity implements SensorEventListener, GameListener {
	private static final int FROM_RADS_TO_DEGS = -57;

	private SensorManager mSensorManager;
	private Sensor mSensor;

	private Game game;

	private FrameLayout frameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_activity);

		// Load the sensors
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		this.frameLayout = (FrameLayout) findViewById(R.id.frame_layout);

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
		surfaceView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				game.screenTouched(motionEvent.getX(), motionEvent.getY());
				return true;
			}
		});
		game = new Game(getApplicationContext(), surfaceView.getHolder(), this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		game.pause();
		unregisterListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		game.resume();
		registerListeners();
	}

	/**
	 * Registers the listeners for the sensors
	 */
	private void registerListeners() {
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * Unregisters the listeners for the sensors
	 */
	private void unregisterListeners() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		float[] rotationMatrix = new float[9];
		SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
		int worldAxisX = SensorManager.AXIS_X;
		int worldAxisZ = SensorManager.AXIS_Z;
		float[] adjustedRotationMatrix = new float[9];
		SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
		float[] orientation = new float[3];
		SensorManager.getOrientation(adjustedRotationMatrix, orientation);

		float roll = orientation[2] * FROM_RADS_TO_DEGS;

		if (roll >= -90 && roll <= 90) {
			//Log.i("DoodleSurfaceView", "Rotation = " + roll);
			this.game.rotationChanged(roll);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public void onSceneChanged(Scene newScene) {
		// Should remove all the overlays, and add the new overlays
		for (Overlay overlay : newScene.getOverlays()) {
			frameLayout.addView(overlay.getOverlayView());
		}
	}
}
