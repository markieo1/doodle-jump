package com.anthony.marco.doodlelibrary.logic.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.util.SparseArray;

import com.anthony.marco.doodlelibrary.graphics.view.Overlay;
import com.anthony.marco.doodlelibrary.logic.DoodleGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Scene {
	protected DoodleGame game;

	protected SparseArray<Object> values;

	protected List<Overlay> overlays;

	public Scene(DoodleGame game) {
		this.game = game;
		this.values = new SparseArray<>();
		this.overlays = new ArrayList<>();
	}

	public void loadResources(Context context) {
		for (Overlay overlay : overlays) {
			overlay.loadResources(context);
		}
	}

	public abstract void start();

	public void update(double deltaTime) {
		for (Overlay overlay : overlays) {
			overlay.update(deltaTime);
		}
	}

	public void draw(Canvas canvas) {
		for (Overlay overlay : overlays) {
			overlay.draw(canvas);
		}
	}

	public abstract void cleanup();

	public int getWidth() {
		return game.getScreenWidth();
	}

	public int getHeight() {
		return game.getScreenHeight();
	}

	public DoodleGame getGame() {
		return game;
	}

	public SparseArray<Object> getValues() {
		return values;
	}

	public void onScreenTouched(float x, float y) {
		for (Overlay overlay : overlays) {
			overlay.onTouch(x, y);
		}
	}

	public void onRotationChanged(float newRotation) {
	}

}
