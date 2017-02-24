package com.anthony.marco.doodlelibrary.graphics.animation;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by marco on 24-2-2017.
 */

public class Animation {
	/**
	 * The frames for this animation
	 */
	private ArrayList<AnimationFrame> mFrames;

	/**
	 * Determines if the animation is looping
	 */
	private boolean looping;

	/**
	 * The total length of the animation
	 */
	private float animationLength;

	public Animation() {
		mFrames = new ArrayList<>();
		looping = false;
		animationLength = 0.0f;
	}

	/**
	 * Adds frames to the animation
	 * @param frames The frames to be added
	 */
	public void addFrames(AnimationFrame... frames) {
		for (AnimationFrame frame : frames) {
			mFrames.add(frame);
			animationLength += frame.getFrameTime();
		}
	}

	/**
	 * Gets the frame to display now
	 * @param animationTime The time running the animation
	 * @return The frame to display
	 */
	public AnimationFrame getFrame(float animationTime) {
		AnimationFrame result = null;
		final float length = getAnimationLength();
		if (length > 0.0f) {
			final ArrayList<AnimationFrame> frames = mFrames;
			final int frameCount = frames.size();
			result = frames.get(frameCount - 1);

			if (frameCount > 1) {
				float currentTime = 0.0f;
				float cycleTime = animationTime;
				if (isLooping()) {
					cycleTime = animationTime % length;
				}

				if (cycleTime < length) {
					for (int x = 0; x < frameCount; x++) {
						AnimationFrame frame = frames.get(x);
						currentTime += frame.getFrameTime();
						if (currentTime > cycleTime) {
							result = frame;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public float getAnimationLength() {
		return animationLength;
	}

	/**
	 * Creates an animation from a bitmap sprite sheet
	 * Incorrect bitmap width may be reported, this is because DPI scaling is applied when using the BitmapFactory.
	 * Add the inScaled = false option when decoding the resource. This allows the bitmap factory to not change the size of the bitmap
	 *
	 * @param spriteSheet The bitmap containing all the sprites
	 * @param row The row on the bitmap to get the animation for
	 * @param tileSizeX The size/width per sprite
	 * @param tileSizeY The size/height per sprite
	 * @param frameDuration The duration per frame
	 * @return An animation containing all the frames of the sprite sheet.
	 */
	public static Animation fromSpriteSheet(Bitmap spriteSheet, int row, int tileSizeX, int tileSizeY, float frameDuration){
		Animation animation = new Animation();

		// get the size
		int bitmapWidth = spriteSheet.getWidth();

		// Calculate the amount of images
		int amountOfImages = bitmapWidth / tileSizeX;
		for (int i = 0; i < amountOfImages; i++) {
			// Now we loop through all of them to get the correct one
			int startX = (i * tileSizeX);
			int startY = (row * tileSizeY);

			Bitmap sprite = Bitmap.createBitmap(spriteSheet, startX, startY, tileSizeX, tileSizeY);

			AnimationFrame frame = new AnimationFrame(sprite, frameDuration);
			animation.addFrames(frame);
		}

		return animation;
	}
}
