package com.anthony.marco.doodlelibrary.graphics.animation;

import android.graphics.Bitmap;

/**
 * Created by marco on 24-2-2017.
 */

public class AnimationFrame {
	/**
	 * The bitmap to display for this frame
	 */
	private Bitmap bitmap;

	/**
	 * The time (MILLISECONDS) this bitmap is allowed to be displayed
	 */
	private float frameTime;

	public AnimationFrame(Bitmap bitmap, float frameTime) {
		this.bitmap = bitmap;
		this.frameTime = frameTime;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public float getFrameTime() {
		return frameTime;
	}
}
