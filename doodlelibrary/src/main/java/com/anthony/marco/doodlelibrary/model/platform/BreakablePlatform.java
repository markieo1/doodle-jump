package com.anthony.marco.doodlelibrary.model.platform;

import android.graphics.Bitmap;

/**
 * Created by marco on 28-1-2017.
 */

public class BreakablePlatform extends Platform {
	private Bitmap brokenBitmap;

	public BreakablePlatform(float x, float y, float width, float height, Bitmap normalBitmap, Bitmap brokenBitmap) {
		super(x, y, width, height, normalBitmap);
		this.brokenBitmap = brokenBitmap;
		this.setBroken(false);
	}

	@Override
	public void update(double dt) {
		if (this.isBroken())
			setImage(brokenBitmap);

		super.update(dt);
	}
}
