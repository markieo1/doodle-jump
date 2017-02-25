package com.anthony.marco.doodlelibrary.logic.scene;

import android.content.Context;
import android.graphics.Canvas;

import com.anthony.marco.doodlelibrary.logic.DoodleGame;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Scene {
	protected DoodleGame game;

	public Scene(DoodleGame game) {
		this.game = game;
	}

	public void loadResources(Context context) {
	}

	public abstract void start();

	public abstract void update(double deltaTime);

	public abstract void draw(Canvas canvas);

	public abstract void cleanup();

	public void onScreenTouched(float x, float y) {
	}

	public void onRotationChanged(float newRotation) {
	}

}
