package com.anthony.marco.doodlelibrary.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.anthony.marco.doodlelibrary.logic.ScrollingCamera;
import com.anthony.marco.doodlelibrary.view.DoodleSurfaceView;

/**
 * Created by anthony on 3-1-2017.
 */

public class Entity {
	private final String TAG = "Entity";

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

	/**
	 * The velocity on the X axis
	 */
	protected float velocityX;

	/**
	 * The velocity on the Y axis
	 */
	protected float velocityY;

	/**
	 * The Entity bitmap image
	 */
	protected Bitmap image;

	protected Animation currentAnimation;

	private float animationTime;

	public Entity(float x, float y, float width, float height) {
		this(x, y, width, height, 0, 0);
	}

	public Entity(float x, float y, float width, float height, float velocityX, float velocityY) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.currentAnimation = null;

		Log.i(TAG, "New Entity created, pos = " + x + ", " + y + ", width = " + width + ", height = " + height + ", velocityX = " + velocityX + ", velocityY = " + velocityY);
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

	public float getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	/**
	 * Draws this instance
	 *
	 * @param camera The camera to use
	 * @param canvas The canvas to draw onto
	 */
	public void draw(ScrollingCamera camera, Canvas canvas) {
		Bitmap image = getImage();

		if(image == null)
			return;

		float relativeYPos = camera.getRelativeYPosition(getY());
		canvas.drawBitmap(getImage(), null, new RectF(getX(), relativeYPos, getX() + getWidth(), relativeYPos + getHeight()), null);
	}

	/**
	 * Updates this instance by adding the velocity
	 */
	public void update(double dt) {
		animationTime += dt;

		AnimationFrame currentFrame = currentAnimation.getFrame(animationTime);
		if(currentFrame != null){
			setImage(currentFrame.getTexture());
		}

		float newX = getX() + (float) (getVelocityX() * dt * DoodleSurfaceView.TARGET_FPS / DoodleSurfaceView.SECOND);
		float newY = getY() + (float) (getVelocityY() * dt * DoodleSurfaceView.TARGET_FPS / DoodleSurfaceView.SECOND);

		setX(newX);
		setY(newY);
	}

	/**
	 * Checks if this Instance is in the Y camera bounds
	 *
	 * @param camera The camera to use for checking
	 * @return True if in screen on Y axis, else false
	 */
	public boolean isInScreen(ScrollingCamera camera) {
		float relativeYPosition = camera.getRelativeYPosition(getY());
		return camera.getScreenHeight() > relativeYPosition;
	}

	public void setAnimation(Animation animation){
		this.currentAnimation = animation;
	}
}
