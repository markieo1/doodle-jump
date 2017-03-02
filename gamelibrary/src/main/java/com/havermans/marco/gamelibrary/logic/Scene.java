package com.havermans.marco.gamelibrary.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.SparseArray;

import com.havermans.marco.gamelibrary.Game;
import com.havermans.marco.gamelibrary.graphics.Overlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Scene {
	protected Game game;

	protected SparseArray<Object> values;

	protected List<Overlay> overlays;

	public Scene(Game game) {
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

	public abstract void draw(Canvas canvas);

	public abstract void cleanup();

	public int getWidth() {
		return game.getScreenWidth();
	}

	public int getHeight() {
		return game.getScreenHeight();
	}

	public SparseArray<Object> getValues() {
		return values;
	}

	public void onScreenTouched(float x, float y) {
	}

	public void onRotationChanged(float newRotation) {
	}

	public List<Overlay> getOverlays() {
		return this.overlays;
	}

}
