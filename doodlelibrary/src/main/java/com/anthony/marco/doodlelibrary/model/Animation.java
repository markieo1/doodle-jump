package com.anthony.marco.doodlelibrary.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marco on 24-2-2017.
 */

public class Animation {
	private final static int LINEAR_SEARCH_CUTOFF = 16;

	private ArrayList<AnimationFrame> mFrames;
	private float[] mFrameStartTimes;
	private boolean mLoop;
	private float mLength;

	public Animation(int frameCount){
		mFrames = new ArrayList<>(frameCount);
		mFrameStartTimes = new float[frameCount];
		mLoop = false;
		mLength = 0.0f;
	}

	public void addFrame(AnimationFrame frame){
		mFrameStartTimes[mFrames.size()] = mLength;
		mFrames.add(frame);
		mLength += frame.getHoldTime();
	}

	public float getLength() {
		return mLength;
	}

	public void setLoop(boolean loop) {
		mLoop = loop;
	}

	public boolean getLoop() {
		return mLoop;
	}

	public AnimationFrame getFrame(float animationTime) {
		AnimationFrame result = null;
		final float length = mLength;
		if (length > 0.0f) {
			final ArrayList<AnimationFrame> frames = mFrames;
			//assert frames.getCount() == frames.getCapacity();
			final int frameCount = frames.size();
			result = frames.get(frameCount - 1);

			if (frameCount > 1) {
				float currentTime = 0.0f;
				float cycleTime = animationTime;
				if (mLoop) {
					cycleTime = animationTime % length;
				}

				if (cycleTime < length) {
					// When there are very few frames it's actually slower to do a binary search
					// of the frame list.  So we'll use a linear search for small animations
					// and only pull the binary search out when the frame count is large.
					if (mFrameStartTimes.length > LINEAR_SEARCH_CUTOFF) {
						int index = Arrays.binarySearch(mFrameStartTimes, cycleTime);
						if (index < 0) {
							index = -(index + 1) - 1;
						}
						result = frames.get(index);
					} else {
						for (int x = 0; x < frameCount; x++) {
							AnimationFrame frame = frames.get(x);
							currentTime += frame.getHoldTime();
							if (currentTime > cycleTime) {
								result = frame;
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}
}
