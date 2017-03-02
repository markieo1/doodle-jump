package com.havermans.marco.gamelibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.havermans.marco.gamelibrary.logic.Scene;

/**
 * Created by marco on 27-2-2017.
 */

public class Game implements Runnable, SurfaceHolder.Callback {
	private final String TAG = "Game";

	private volatile boolean isRunning;

	public static final double SECOND = 1000.0;
	public static final double TARGET_FPS = 60.0;
	private static final double FRAME_PERIOD = SECOND / TARGET_FPS;

	private Thread gameThread;

	/**
	 * The screen width used for the game
	 */
	private int screenWidth;

	/**
	 * The screen height used for the game
	 */
	private int screenHeight;

	private SurfaceHolder surfaceHolder;

	private Context context;

	protected Scene currentScene;

	private GameListener gameListener;

	public Game(Context context, SurfaceHolder surfaceHolder, GameListener gameListener) {
		this.surfaceHolder = surfaceHolder;
		this.gameListener = gameListener;
		this.context = context;
		this.surfaceHolder.addCallback(this);
		gameThread = null;
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
		this.screenWidth = surfaceFrame.width();
		this.screenHeight = surfaceFrame.height();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		pause();
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

			while (accumulator >= dt) {
				update(dt);
				accumulator -= dt;
			}

			Canvas canvas = surfaceHolder.lockCanvas();
			canvas.drawColor(Color.BLACK);
			draw(canvas);

			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * Updates all entities
	 */
	public void update(double dt) {
		currentScene.update(dt);
	}

	/**
	 * Draws the camera using the specified canvas
	 *
	 * @param canvas The canvas to draw onto
	 */
	public void draw(Canvas canvas) {
		currentScene.draw(canvas);
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

	public void switchScene(Scene newScene) {
		this.currentScene = newScene;
		this.gameListener.onSceneChanged(newScene);
	}

	public Scene getCurrentScene() {
		return currentScene;
	}

	/**
	 * Gets the current screenWidth
	 *
	 * @return The current screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Gets the current screenHeight
	 *
	 * @return The current screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	public void screenTouched(float xPosition, float yPosition) {
		currentScene.onScreenTouched(xPosition, yPosition);
	}

	public void rotationChanged(float newRotation) {
		this.currentScene.onRotationChanged(newRotation);
	}
}
