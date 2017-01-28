package com.anthony.marco.doodlelibrary.model.platform;

import android.graphics.Bitmap;

import com.anthony.marco.doodlelibrary.model.Entity;

/**
 * Created by marco on 28-1-2017.
 */

public class Platform extends Entity {
	private boolean isBroken;

	public Platform(float x, float y, float width, float height, Bitmap image) {
		super(x, y, width, height, image);
		this.isBroken = false;
	}

	/**
	 * Gets the jump boost for the platform
	 *
	 * @return The jump boost for the platform
	 */
	public float getJumpBoost() {
		return 0;
	}

	/**
	 * Determines if the plaform is broken
	 * @return
	 */
	public boolean isBroken() {
		return isBroken;
	}

	/**
	 * Sets the is broken property for this platform
	 * @param broken If the platform should be broken provide true
	 */
	public void setBroken(boolean broken) {
		isBroken = broken;
	}
}
