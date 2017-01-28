package com.anthony.marco.doodlelibrary.model;

import android.graphics.Bitmap;

/**
 * Created by marco on 28-1-2017.
 */

public class Platform extends Entity {
	public Platform(float x, float y, float width, float height, Bitmap image) {
		super(x, y, width, height, image);
	}

	/**
	 * Gets the jump boost for the platform
	 *
	 * @return The jump boost for the platform
	 */
	public float getJumpBoost() {
		return 0;
	}
}
