package com.anthony.marco.doodlelibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.anthony.marco.doodlelibrary.logic.DoodleGame;
import com.anthony.marco.doodlelibrary.listener.DoodleListener;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback, SensorEventListener {
	private static final String TAG = "DoodleSurfaceView";
	private SurfaceHolder surfaceHolder;
	private static final int FROM_RADS_TO_DEGS = -57;

	private volatile boolean isRunning;
	private DoodleGame doodleGame;
	private Thread gameThread;

	public static final double SECOND = 1000.0;
	public static final double TARGET_FPS = 60.0;
	private static final double FRAME_PERIOD = SECOND / TARGET_FPS;

	public DoodleSurfaceView(Context context) {
		super(context);
		initialize();
	}

	public DoodleSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public DoodleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize();
	}


	private void initialize() {
		gameThread = null;
		this.surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		this.doodleGame = new DoodleGame(getContext());
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
		this.doodleGame.screenSizeChanged(surfaceFrame.width(), surfaceFrame.height());
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		pause();
	}

	public void startGame(DoodleListener doodleListener) {
		this.doodleGame.startGame(doodleListener);
	}

	public void stopGame() {
		this.doodleGame.stopGame();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.doodleGame.screenTouched(event.getX(), event.getY());
		return super.onTouchEvent(event);
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
			this.doodleGame.rotationChanged(roll);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public void run() {
		long last = System.currentTimeMillis();
		double dt = FRAME_PERIOD;
		double accumulator = 0;

		while (isRunning) {
			if (!surfaceHolder.getSurface().isValid())
				continue;

			long now = System.currentTimeMillis();
			long passed = now - last;
			last = now;
			accumulator += passed;

			while(accumulator >= dt){
				doodleGame.update(dt);
				accumulator -= dt;
			}

			Canvas canvas = surfaceHolder.lockCanvas();
			canvas.drawColor(Color.BLACK);
			doodleGame.draw(canvas);

			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause() {
		Log.i(TAG, "Pause is called.");
		isRunning = false;
		try {
			// Join lets current thread wait
			gameThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Error: joining thread");
		}
	}

	public void resume() {
		Log.i(TAG, "Resume is called.");
		isRunning = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
}