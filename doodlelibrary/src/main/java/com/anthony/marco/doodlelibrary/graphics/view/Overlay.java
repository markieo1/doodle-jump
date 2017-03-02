package com.anthony.marco.doodlelibrary.graphics.view;

import android.content.Context;
import android.graphics.Canvas;

import com.anthony.marco.doodlelibrary.logic.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Overlay {
	protected Scene scene;

	protected List<GButton> buttons;

	public Overlay(Scene scene) {
		this.buttons = new ArrayList<>();
		this.scene = scene;
	}

	public void loadResources(Context context) {
	}

	public void update(double dt) {
		for (GButton button : buttons) {
			button.update(dt);
		}
	}

	public void draw(Canvas canvas) {
		for (GButton button : buttons) {
			button.draw(canvas);
		}
	}

	public void onTouch(float x, float y) {
		for (GButton button : buttons) {
			// Check if there is a click on it
			if (button.getDrawingRectangle().contains(x, y)) {
				button.click();
			}
		}
	}
}
