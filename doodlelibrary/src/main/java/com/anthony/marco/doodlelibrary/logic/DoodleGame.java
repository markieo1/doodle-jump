package com.anthony.marco.doodlelibrary.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.anthony.marco.doodlelibrary.listener.DoodleListener;
import com.anthony.marco.doodlelibrary.listener.ScreenListener;
import com.anthony.marco.doodlelibrary.logic.scene.GameplayScene;
import com.anthony.marco.doodlelibrary.logic.scene.Scene;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleGame implements ScreenListener {
	private final String TAG = "DoodleGame";

	/**
	 * The screen width used for the game
	 */
	private int screenWidth;

	/**
	 * The screen height used for the game
	 */
	private int screenHeight;

	/**
	 * The listener that handles game changes
	 */
	private DoodleListener doodleListener;

	/**
	 * The Android context
	 */
	private Context context;

	private Scene currentScene;

	public DoodleGame(Context context) {
		this.context = context;
		this.currentScene = new GameplayScene(this);
	}

	/**
	 * Starts the game
	 *
	 * @param doodleListener The listener to make callbacks to
	 */
	public void startGame(final DoodleListener doodleListener) {
		Log.i(TAG, "Start game called!");
		this.doodleListener = doodleListener;

		loadResources();
		this.currentScene.start();
	}

	/**
	 * Stops the game
	 */
	public void stopGame(int finalScore) {
		Log.i(TAG, "Game stopped!");
		currentScene.cleanup();

		if (doodleListener != null) {
			Log.i(TAG, "Callback to gameOver");
			// Make callback saying the game is over
			doodleListener.gameOver(Math.round(finalScore));
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


	/**
	 * Loads all the resources that are needed
	 */
	private void loadResources() {
		Log.i(TAG, "Starting load resources.");

		currentScene.loadResources(context);
		Log.i(TAG, "Done loading resources.");
	}

	@Override
	public void screenTouched(float xPosition, float yPosition) {
		currentScene.onScreenTouched(xPosition, yPosition);
	}

	@Override
	public void screenSizeChanged(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
	}

	@Override
	public void rotationChanged(float newRotation) {
		this.currentScene.onRotationChanged(newRotation);
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
}
