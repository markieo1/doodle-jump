package com.anthony.marco.doodlelibrary.graphics.animation;

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
}
