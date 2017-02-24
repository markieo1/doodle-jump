package com.anthony.marco.doodlelibrary.model;

import android.graphics.Bitmap;

/**
 * Created by marco on 24-2-2017.
 */

public class AnimationFrame {
	private Bitmap texture;
	private float holdTime;

	public AnimationFrame(Bitmap texture, float holdTime) {
		this.texture = texture;
		this.holdTime = holdTime;
	}

	public Bitmap getTexture() {
		return texture;
	}

	public float getHoldTime() {
		return holdTime;
	}
}
