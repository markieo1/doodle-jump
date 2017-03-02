package com.havermans.marco.gamelibrary.graphics;

import android.content.Context;
import android.view.View;

import com.havermans.marco.gamelibrary.logic.Scene;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Overlay {
	protected Scene scene;

	protected View overlayView;

	public Overlay(Scene scene) {
		this.scene = scene;
	}

	public void loadResources(Context context) {
	}

	public void update(double dt) {
	}

	public void onTouch(float x, float y) {
	}

	public View getOverlayView() {
		return overlayView;
	}
}
