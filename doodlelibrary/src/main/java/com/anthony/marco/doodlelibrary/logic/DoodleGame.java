package com.anthony.marco.doodlelibrary.logic;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;

import com.anthony.marco.doodlelibrary.listener.DoodleListener;
import com.anthony.marco.doodlelibrary.logic.scene.GameplayScene;
import com.havermans.marco.gamelibrary.Game;
import com.havermans.marco.gamelibrary.GameListener;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleGame extends Game {

	/**
	 * The listener that handles game changes
	 */
	private DoodleListener doodleListener;

	public DoodleGame(Context context, SurfaceHolder surfaceHolder, GameListener gameListener) {
		super(context, surfaceHolder, gameListener);

		switchScene(new GameplayScene(this));
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

	@Override
	public void screenTouched(float xPosition, float yPosition) {
		currentScene.onScreenTouched(xPosition, yPosition);
	}

	@Override
	public void rotationChanged(float newRotation) {
		this.currentScene.onRotationChanged(newRotation);
	}



	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		pause();
	}


}
