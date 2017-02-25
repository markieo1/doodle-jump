package com.anthony.marco.doodlelibrary.graphics.view;

import android.content.Context;
import android.graphics.Canvas;

import com.anthony.marco.doodlelibrary.logic.scene.Scene;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Overlay {
	protected Scene scene;

	public Overlay(Scene scene) {
		this.scene = scene;
	}

	public void loadResources(Context context) {
	}

	public void update(double dt) {
	}

	public void draw(Canvas canvas) {
	}

	public void onTouch(float x, float y) {
	}
}
