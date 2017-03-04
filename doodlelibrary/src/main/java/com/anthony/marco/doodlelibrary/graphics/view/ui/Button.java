package com.anthony.marco.doodlelibrary.graphics.view.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.anthony.marco.doodlelibrary.graphics.animation.Animation;
import com.anthony.marco.doodlelibrary.graphics.animation.AnimationFrame;
import com.anthony.marco.doodlelibrary.graphics.util.TextHelper;
import com.anthony.marco.doodlelibrary.listener.OnClickListener;

/**
 * Created by marco on 2-3-2017.
 */

public class Button extends GuiItem {
	/**
	 * The width
	 */
	protected float width;

	/**
	 * The height
	 */
	protected float height;

	protected String text;

	protected RectF drawingRectangle;

	protected Animation backgroundAnimation;

	private float animationTime;

	private float textPositionX;
	private float textPositionY;

	private OnClickListener onClickListener;

	public Button(float x, float y, float width, float height, String text, Bitmap bitmap, Paint paint) {
		this(x, y, width, height, text, Animation.fromBitmaps(1, bitmap), paint);
	}

	public Button(float x, float y, float width, float height, String text, Animation background, Paint paint) {
		super(x, y, paint);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.backgroundAnimation = background;
		this.drawingRectangle = new RectF();
		this.paint = paint;
		this.textPositionX = x + TextHelper.getXToCenterText(text, paint, width);
		this.textPositionY = y + TextHelper.getYToCenterText(text, paint, height);
		this.drawingRectangle.set(x, y, x + width, y + height);
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
	}

	@Override
	public void draw(Canvas canvas) {
		if (backgroundAnimation != null) {
			AnimationFrame currentFrame = backgroundAnimation.getFrame(animationTime);
			if (currentFrame != null)
				canvas.drawBitmap(currentFrame.getBitmap(), null, drawingRectangle, paint);
		}

		if (text != null) {
			// Now we draw the text
			canvas.drawText(text, textPositionX, textPositionY, paint);
		}
	}

	public void click() {
		if (onClickListener != null)
			onClickListener.onClick();
	}
}
