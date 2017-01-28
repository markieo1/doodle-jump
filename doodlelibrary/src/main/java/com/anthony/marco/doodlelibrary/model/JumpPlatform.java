package com.anthony.marco.doodlelibrary.model;

import android.graphics.Bitmap;

/**
 * Created by marco on 28-1-2017.
 */

public class JumpPlatform extends Platform {
	public JumpPlatform(float x, float y, float width, float height, Bitmap image) {
		super(x, y, width, height, image);
	}

	@Override
	public float getJumpBoost() {
		return 60;
	}
}
