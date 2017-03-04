package com.anthony.marco.doodlelibrary.graphics.view.ui;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by marco on 4-3-2017.
 */

public abstract class GuiItem {
	/**
	 * The X position
	 */
	protected float x;

	/**
	 * The Y position
	 */
	protected float y;

	/**
	 * The paint to use for drawing
	 */
	protected Paint paint;

	public GuiItem(float x, float y) {
		this(x, y, new Paint());
	}

	public GuiItem(float x, float y, Paint paint) {
		this.x = x;
		this.y = y;
		this.paint = paint;
	}


	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void update(double dt) {
	}

	public abstract void draw(Canvas canvas);
}
