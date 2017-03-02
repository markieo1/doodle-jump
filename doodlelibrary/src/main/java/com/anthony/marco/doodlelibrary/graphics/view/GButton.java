package com.anthony.marco.doodlelibrary.graphics.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.anthony.marco.doodlelibrary.graphics.animation.Animation;
import com.anthony.marco.doodlelibrary.graphics.animation.AnimationFrame;
import com.anthony.marco.doodlelibrary.listener.OnClickListener;

/**
 * Created by marco on 2-3-2017.
 */

public class GButton {
	/**
	 * The X position
	 */
	protected float x;

	/**
	 * The Y position
	 */
	protected float y;

	/**
	 * The width
	 */
	protected float width;

	/**
	 * The height
	 */
	protected float height;

	protected RectF drawingRectangle;

	protected Animation currentAnimation;

	private float animationTime;

	private OnClickListener onClickListener;

	public GButton(float x, float y, float width, float height, Bitmap bitmap) {
		this(x, y, width, height, Animation.fromBitmaps(1, bitmap));
	}

	public GButton(float x, float y, float width, float height, Animation animation) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.currentAnimation = animation;
		this.drawingRectangle = new RectF();
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

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public RectF getDrawingRectangle() {
		return drawingRectangle;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void update(double dt) {
		animationTime += dt;
		drawingRectangle.set(getX(), getY(), getX() + getWidth(), getY() + getHeight());
	}

	public void draw(Canvas canvas) {
		if (currentAnimation == null)
			return;

		AnimationFrame currentFrame = currentAnimation.getFrame(animationTime);
		if (currentFrame == null)
			return;

		canvas.drawBitmap(currentFrame.getBitmap(), null, drawingRectangle, null);
	}

	public void click() {
		if (onClickListener != null)
			onClickListener.onClick();
	}
}
